package com.android.test.shell.utest;

import android.annotation.TargetApi;
import android.net.LocalSocket;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by yuchaofei on 15/11/30.
 */
public class UMVirtualTerminal {
	private static final String TAG = "UMVirtualTerminal";

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 6666;

	private static AtomicBoolean sIsLocalSocket = new AtomicBoolean(true);

	private static final long WAIT_TIME = 100;
	private static AtomicInteger sIndex = new AtomicInteger(1);

	private Lock mLock = new ReentrantLock();
	private Condition mCondition = mLock.newCondition();
	private String mResult = "";
	private SuHandler handler = null;
	HandlerThread handlerThread = new HandlerThread("" + sIndex.incrementAndGet());


	public UMVirtualTerminal() throws IOException, InterruptedException {
		init();
	}

	private void init() throws IOException, InterruptedException {

		handlerThread.start();
		handler = new SuHandler(handlerThread.getLooper());
		if (!handler.isSuccess){
			throw new IOException();
		}
	}

	/**
	 * 采取等待的方式
	 *
	 * @param command
	 * @return
	 * @throws Exception
	 */
	public VTCommandResult runCommand(String command) throws Exception {
		VTCommandResult commandResult = null;
		Log.i(TAG, command);
		command = command + "\necho :RET=$?\n";
		try {
			//Log.d(TAG, "=====lock==========");
			mLock.lock();
			Message message = handler.obtainMessage(SuHandler.MSG_TYPE_SEND, command);
			handler.sendMessage(message);
			//Log.d(TAG, "=====send over start wait==========");
			Log.i(TAG,"命令:"+command);
			Log.i(TAG,"执行开始");
			mCondition.await(WAIT_TIME, TimeUnit.SECONDS);
			Log.i(TAG,"执行结束");
			//Log.d(TAG, "=====wait end==========");
			String inp = mResult.toString();
			if (inp.equals("")) {
				//throw new Exception("VirtualTerminal time out");
			} else {
				if (inp.contains(":RET=EOF")) {
					close();
				} else {
					if (inp.contains(":RET=0")) {
						Log.i(TAG , "oclf success");
						commandResult = new VTCommandResult(0, inp, "error");
					} else {
						Log.i(TAG, "oclf error");
						commandResult = new VTCommandResult(1, inp, inp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			mLock.unlock();
		}
		//Log.d(TAG, "=====commandResult==========");
		return commandResult;
	}



	public void shutdown() {
		close();
	}

	private void close() {
		handler.sendEmptyMessage(SuHandler.MSG_TYPE_CLOSE);
	}

	public class SuHandler extends Handler {
		private static final int MSG_TYPE_INIT = 0;//初始化
		public static final int MSG_TYPE_SEND = 1;//发送消息
		public static final int MSG_TYPE_CLOSE = 2;//关闭
		private BufferedReader mBufferedReader;
		private LocalSocket mLocalSocket = null;
		private Socket mSocket = null;
		private DataOutputStream mDos = null;
		private StringBuffer tempBuffer = new StringBuffer();
		private InputStream mInputStream = null;
		private boolean isInit = false;

		public boolean isSuccess = false;

		public SuHandler(Looper looper) {
			super(looper);
			if (!isInit) init();
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_TYPE_INIT:
					if (!isInit) init();
					break;
				case MSG_TYPE_SEND:
					send(msg);
					break;
				case MSG_TYPE_CLOSE:
					close();
					break;
				default:
					break;
			}
		}

		/**
		 * 初始化连接，如果LocalSocket好用就优先使用
		 */
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		private void init() {
			try {
				//Log.d(TAG, "initSocket");
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
				mDos = new DataOutputStream(socket.getOutputStream());
				mInputStream = socket.getInputStream();
				mBufferedReader = new BufferedReader(
						new InputStreamReader(mInputStream));
				isInit = true;
				//CheckPoint.endCheckPoint(CheckPoint.POINT_CONN_SU, true);
				mSocket = socket;
				sIsLocalSocket.set(false);
				isSuccess = true;
			} catch (Exception e) {
				isSuccess = false;
				e.printStackTrace();
				//CheckPoint.endCheckPoint(CheckPoint.POINT_CONN_SU, false, null, e);
			}
		}

		private void send(Message msg) {
			String command = (String) msg.obj;
			try {
				mLock.lock();
				if (isInit) {
					tempBuffer.delete(0, tempBuffer.length());
					byte[] bs = command.getBytes("UTF-8");
					ByteBuffer buffer = ByteBuffer.allocate(4);
					buffer.order(ByteOrder.LITTLE_ENDIAN);
					buffer.asIntBuffer().put(1);
					mDos.write(buffer.array());
					mDos.write(bs);
					mDos.flush();
					while (true) {
						String line = mBufferedReader.readLine();
						if (line != null) {
							tempBuffer.append(line + "\n");
							if (line.contains(":RET=")) {//不管服务端发什么，只认一次RET＝为一次记录
								try {
									//Log.d(TAG, "==========InputReader lock===========");
									mResult = tempBuffer.toString();
									//KPLog.d(TAG, mResult);
								} catch (Exception e) {

								} finally {
									break;
								}
							}
						}
					}
				} else {
					mResult = "";
					Log.d(TAG, "SuHandler isInit = fasle");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					mCondition.signalAll();
					//Log.d(TAG, "signal");
				} finally {
					mLock.unlock();
					//Log.d(TAG, "unlock");
				}
			}
		}

		private void close() {
			//Log.d(TAG, "close start");
			try {
				if (mDos != null && mLock.tryLock(2, TimeUnit.SECONDS)) {
					try {
						//Log.d(TAG, "shutdown writeInt");
						ByteBuffer buffer = ByteBuffer.allocate(4);
						buffer.order(ByteOrder.LITTLE_ENDIAN);
						buffer.asIntBuffer().put(3);
						mDos.write(buffer.array());
						mDos.flush();
						//Log.d(TAG, "shutdown writeInt end");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						mLock.unlock();
					}
				}
				if (mDos != null) {
					mDos.close();
					mDos = null;
				}
			} catch (Exception e) {
			} finally {
				try {
					if (mLocalSocket != null) {
						mLocalSocket.close();
						mLocalSocket = null;
					}
					if (mSocket != null) {
						mSocket.close();
						mSocket = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (mInputStream != null) {
							mInputStream.close();
							mInputStream = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			handlerThread.quit();
			//Log.d(TAG, "close end");
		}
	}





}
