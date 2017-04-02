package jmlv.org.Metryc;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntBinaryOperator;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Metryc implements Runnable {
	private static Long idreq;
	private static Long WSidreq;
	private final Vector LowPriority = new Vector();
	private final Vector HighPriority = new Vector();
	private final Vector<Object[]> LowResponses = new Vector();
	private final Vector<Object[]> HighResponses = new Vector();
	private final BlockingQueue<Runnable> blockingQueueLST = new LinkedBlockingDeque<>();
	private final BlockingQueue<Runnable> blockingQueueHST = new LinkedBlockingDeque<>();
	private static String response;
	final Lock lock = new ReentrantLock();
	private final Semaphore semaphore = new Semaphore(1);
	private final Semaphore semaphore2 = new Semaphore(1);
	private static Vector<Object[]> Highrequests = new Vector<Object[]>();
	private static Vector<Object[]> Lowrequests = new Vector<Object[]>();
	private final Semaphore Low = new Semaphore(1);
	private static Integer count;
	private static Integer count2;
	private static Long start_time;
	private static Long timeout;
	private static Boolean autoregulate;
	private static Vector<Long> times = new Vector<Long>();

	// ARBITRATOR INIT
	public Metryc() {
		idreq = (long) 1;
		WSidreq = (long) 1;
		ReadConfig conf = new ReadConfig();
		timeout = conf.getTimeout();
		autoregulate = conf.getAutoregulate();
		if (autoregulate) {
			times.add(timeout);
		}
		// INIT REQUESTS PROCESSING THREAD
		Thread t = new Thread(this);
		t.start();
		// INIT HIGH SPEED THREAD

		startHST();

		// INIT LOW SPEED THREAD
		startLST();
		WebsocketThreads();

	}

	public void WebsocketThreads() {
		count = 0;
		count2 = 0;
		final Thread low = new Thread(new Runnable() {
			@Override
			public void run() {
				// system.out.println("RUNNING");
				while (true) {
					synchronized (Lowrequests) {
						// if(Highrequests.size()<1){
						if (!Lowrequests.isEmpty()) {
							long time = System.currentTimeMillis();
							if (time > (start_time + timeout)) {
								// system.out.println("TIMEOUT");
								start_time = System.currentTimeMillis();
								count = 10;
							}
							if (count >= 10 && count < 12) {
								count = 0;
								try {
									// system.out.println(Lowrequests.size());
									// if(Highrequests.isEmpty()){
									Low.acquire();
									Object[] obj = Lowrequests.get(0);
									Lowrequests.remove(0);
									String data = (String) obj[0];
									if (obj.length < 3) {
										// system.out.println(obj[1].getClass().getSimpleName()
										// + " " + Session.class);
										if ((obj[1].getClass().getSimpleName()).equals("WsSession")) {
											Session p = (Session) obj[1];
											Long start = System.currentTimeMillis();
											while (add(data, p) == null) {
												// system.out.println("WHILE");
											}
											Long end = System.currentTimeMillis();
											if (Highrequests.isEmpty()) {
												if (autoregulate) {
													if(times.size()<5){
														long temptimes = 0;
														for(int i=0; i<times.size();i++){
															temptimes+=times.get(i);
														}
														timeout = (temptimes+ (end - start)) / times.size();
														times.add((end - start));
													}else{
														timeout = (times.get(0) + times.get(1) + times.get(2) + times.get(3)
														+ (end - start)) / 5;
													}
													times.remove(0);
													times.add((end - start));
												}
											}
											Low.release();
										} else {
											// HttpServletResponse rs =
											// (HttpServletResponse) obj[1];
											// try {
											// PrintWriter out = rs.getWriter();
											// String response1;
											// response1 = add(data);
											// out.print(response1);
											// } catch (SecurityComponent e) {
											// // TODO Auto-generated catch
											// // block
											// e.printStackTrace();
											// }
											Vector<Session> p = (Vector<Session>) obj[1];
											Long start = System.currentTimeMillis();
											while (add(data, p) == null) {
												// system.out.println("WHILE");
											}
											Long end = System.currentTimeMillis();
											if (Highrequests.isEmpty()) {
												if (autoregulate) {
													if(times.size()<5){
														long temptimes = 0;
														for(int i=0; i<times.size();i++){
															temptimes+=times.get(i);
														}
														timeout = (temptimes+ (end - start)) / times.size();
														times.add((end - start));
													}else{
														timeout = (times.get(0) + times.get(1) + times.get(2) + times.get(3)
														+ (end - start)) / 5;
													}
													times.remove(0);
													times.add((end - start));
												}
											}
											Low.release();
										}

									} else {
										// ADD SECURITY
									}
								}
								// }
								catch (InterruptedException | SecurityComponent e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							count = 0;
						}
					}
				}
				// }

			}
		});

		final Thread high = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (Highrequests) {
						if (!Highrequests.isEmpty()) {
							try {
								Object[] obj = Highrequests.get(0);
								Highrequests.remove(0);
								String data = (String) obj[0];
								if (obj.length < 3) {
									if ((obj[1].getClass().getSimpleName()).equals("WsSession")) {
										Session p = (Session) obj[1];
										add(data, p);
									} else {
										// HttpServletResponse rs =
										// (HttpServletResponse) obj[1];
										// try {
										// PrintWriter out = rs.getWriter();
										// String response1;
										// response1 = add(data);
										// out.print(response1);
										// } catch (SecurityComponent e) {
										// // TODO Auto-generated catch
										// // block
										// e.printStackTrace();
										// }
										Vector<Session> p = (Vector<Session>) obj[1];
										add(data, p);
									}
								} else {
									// ADD SECURITY
								}
							} catch (IOException | SecurityComponent e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			}
		});
		high.setPriority(Thread.MAX_PRIORITY);
		low.setPriority(Thread.MIN_PRIORITY);
		high.start();
		low.start();
	}

	// INIT OF THE LOW SPEED THREAD WITH MIN_PRIORITY
	public void startLST() {
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						synchronized (blockingQueueLST) {
							blockingQueueLST.take().run();
						}
					} catch (InterruptedException ex) {
						return;
					}
				}
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	// INIT OF THE HIGH SPEED THREAD WITH MAX_PRIORITY
	public void startHST() {
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						synchronized (blockingQueueHST) {
							blockingQueueHST.take().run();
						}
					} catch (InterruptedException ex) {
						return;
					}
				}
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	// REQUESTS PROCESSING THREAD
	public void run() {
		while (true) {
			synchronized (HighPriority) {
				if (!HighPriority.isEmpty()) {
					// system.out.println("HIGH");
					JsonObject NextRequest = (JsonObject) HighPriority.elementAt(0);
					addtoHST(NextRequest);
					HighPriority.remove(0);
				}
			}
			synchronized (LowPriority) {
				if (!LowPriority.isEmpty()) {
					// system.out.println("LOW");
					JsonObject NextRequest = (JsonObject) LowPriority.elementAt(0);
					addtoLST(NextRequest);
					LowPriority.remove(0);
				}
			}

		}
	}

	public String getWSHighResponse(Object[] id) throws IOException {
		if ((long) id[0] == (long) 0) {
			for (int i = 0; i < HighResponses.size(); i++) {
				Object[] obj = HighResponses.elementAt(i);
				Long idc = (Long) obj[1];
				String response = (String) obj[0];
				if (idc == id[1]) {
					HighResponses.remove(i);
					Vector<Session> peer = (Vector<Session>) id[2];
					synchronized (peer) {

						for (int x = 0; x < peer.size(); x++) {
							Session p = peer.get(x);
							synchronized (p) {
								p.getBasicRemote().sendText(response);
							}
						}
					}
					// system.out.println("Response: "+ response);
					semaphore2.release();
					return response;
				}
			}
			Object[] newid = { (long) 3, (long) id[1], (Vector<Session>) id[2] };
			while (HighResponses.size() < 1) {
			}
			return getWSHighResponse(newid);
		}
		// REPEAT SECTION
		if ((long) id[0] == (long) 3) {
			for (int i = 0; i < HighResponses.size(); i++) {
				Object[] obj = HighResponses.elementAt(i);
				Long idc = (long) obj[1];
				String response = (String) obj[0];
				if ((long) idc == (long) id[1]) {
					HighResponses.remove(i);
					Vector<Session> peer = (Vector<Session>) id[2];
					synchronized (peer) {
						for (int x = 0; x < peer.size(); x++) {
							Session p = peer.get(x);
							synchronized (p) {
								p.getBasicRemote().sendText(response);
							}
						}
					}
					// system.out.println("Response: "+ response);
					semaphore2.release();
					return response;

				}
			}
			while (HighResponses.size() < 1) {
			}
			return getWSHighResponse(id);
		}
		return response;
	}

	public String getHighResponse(Long[] id) {
		if (id[0] == (long) 0) {
			for (int i = 0; i < HighResponses.size(); i++) {
				Object[] obj = HighResponses.elementAt(i);
				Long idc = (Long) obj[1];
				String response = (String) obj[0];
				if (idc == id[1]) {
					HighResponses.remove(i);
					semaphore2.release();
					return response;
				}
			}
			Long[] newid = { (long) 3, id[1] };
			while (HighResponses.size() < 1) {
			}
			return getHighResponse(newid);

		}

		// REPEAT SECTION
		if (id[0] == (long) 3) {
			for (int i = 0; i < HighResponses.size(); i++) {
				Object[] obj = HighResponses.elementAt(i);
				Long idc = (long) obj[1];
				String response = (String) obj[0];
				if ((long) idc < (long) id[1]) {
				}
				if ((long) idc == (long) id[1]) {
					HighResponses.remove(i);
					semaphore2.release();
					return response;
				}
			}
			while (HighResponses.size() < 1) {
			}
			return getHighResponse(id);
		}
		return response;
	}

	// THE getResponse METHOD RECEIVES THE ID OF THE REQUEST AND WAITS UNTIL IS
	// FINISHED, RELEASES THE SEMAPHORE
	public String getLowResponse(Long[] id) {
		if (id[0] == (long) 0) {

			for (int i = 0; i < LowResponses.size(); i++) {
				Object[] obj = LowResponses.elementAt(i);
				Long idc = (Long) obj[1];
				String response = (String) obj[0];

				if (idc == id[1]) {
					LowResponses.remove(i);
					semaphore.release();
					return response;
				}
			}
			Long[] newid = { (long) 3, id[1] };
			while (LowResponses.size() < 1) {
			}
			return getLowResponse(newid);

		}

		if (id[0] == (long) 3) {
			for (int i = 0; i < LowResponses.size(); i++) {
				// system.out.println("I: " + i + "lowSize: " +
				// LowResponses.size());
				Object[] obj = LowResponses.elementAt(i);
				Long idc = (long) obj[1];
				String response = (String) obj[0];
				if ((long) idc < (long) id[1]) {
				}
				if ((long) idc == (long) id[1]) {
					LowResponses.remove(i);
					semaphore.release();
					return response;

				}
			}
			while (LowResponses.size() < 1) {
			}
			return getLowResponse(id);
		}
		return response;
	}

	public String getWSLowResponse(Object[] id) throws IOException {
		if ((long) id[0] == (long) 0) {

			for (int i = 0; i < LowResponses.size(); i++) {
				Object[] obj = LowResponses.elementAt(i);
				Long idc = (Long) obj[1];
				String response = (String) obj[0];

				if (idc == id[1]) {
					LowResponses.remove(i);
					Vector<Session> peer = (Vector<Session>) id[2];
					synchronized (peer) {
						for (int x = 0; x < peer.size(); x++) {
							Session p = peer.get(x);
							synchronized (p) {
								p.getBasicRemote().sendText(response);
							}
						}
					}
					// system.out.println("Response: "+ response);
					semaphore.release();
					return response;
				}
			}
			Object[] newid = { (long) 3, (long) id[1], (Vector<Session>) id[2] };
			while (LowResponses.size() < 1) {
			}
			return getWSLowResponse(newid);

		}

		if ((long) id[0] == (long) 3) {
			for (int i = 0; i < LowResponses.size(); i++) {
				Object[] obj = LowResponses.elementAt(i);
				Long idc = (long) obj[1];
				String response = (String) obj[0];
				if ((long) idc < (long) id[1]) {
				}
				if ((long) idc == (long) id[1]) {
					LowResponses.remove(i);
					Vector<Session> peer = (Vector<Session>) id[2];
					synchronized (peer) {
						for (int x = 0; x < peer.size(); x++) {
							Session p = peer.get(x);
							synchronized (p) {
								p.getBasicRemote().sendText(response);
							}
						}
					}
					// system.out.println("Response: "+ response);
					semaphore.release();
					return response;

				}
			}
			while (LowResponses.size() < 1) {
			}
			return getWSLowResponse(id);

		}
		return response;
	}

	// ADD WS REQUESTS TO REQUEST THREADS
	public void setRequest(String message, Session p) {
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject request = jelement.getAsJsonObject();
		JsonObject high = request.getAsJsonObject("high");
		JsonObject low = request.getAsJsonObject("low");
		String HighMethod = "";
		String LowMethod = "";
		Object[] req = { message, p };
		if (high != null)
			HighMethod = high.get("package").getAsString();
		if (low != null)
			LowMethod = low.get("package").getAsString();
		if (!HighMethod.equals("")) {
			count2++;
			if (count > (count2 * 2)) {
				count2 = 0;
				count += 1;
			}
			Highrequests.add(req);
		}
		if (!LowMethod.equals("")) {
			if (count == 0) {
				start_time = System.currentTimeMillis();
			}
			count++;
			Lowrequests.add(req);
			// waitLow(req);
		}

	}

	public void setRequest(String message, Vector<Session> peer) {
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject request = jelement.getAsJsonObject();
		JsonObject high = request.getAsJsonObject("high");
		JsonObject low = request.getAsJsonObject("low");
		String HighMethod = "";
		String LowMethod = "";
		Object[] req = { message, peer };
		if (high != null)
			HighMethod = high.get("package").getAsString();
		if (low != null)
			LowMethod = low.get("package").getAsString();
		if (!HighMethod.equals("")) {
			count2++;
			if (count > (count2 * 2)) {
				count2 = 0;
				count += 1;
			}
			Highrequests.add(req);
		}
		if (!LowMethod.equals("")) {
			if (count == 0) {
				start_time = System.currentTimeMillis();
			}
			count++;
			Lowrequests.add(req);
			// waitLow(req);
		}
	}

	// ADD HTTP REQUESTS TO THREADS
	public void setRequest(String message, HttpServletResponse p) {
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject request = jelement.getAsJsonObject();
		JsonObject high = request.getAsJsonObject("high");
		JsonObject low = request.getAsJsonObject("low");
		String HighMethod = "";
		String LowMethod = "";
		Object[] req = { message, p };
		if (high != null)
			HighMethod = high.get("package").getAsString();
		if (low != null)
			LowMethod = low.get("package").getAsString();
		if (!HighMethod.equals("")) {
			count2++;
			if (count2 > (count * 2)) {
				count += 1;
			}

			Highrequests.add(req);
		}
		if (!LowMethod.equals("")) {
			if (count == 0) {
				start_time = System.currentTimeMillis();
			}
			count++;
			Lowrequests.add(req);
			// waitLow(req);
		}

	}

	// ADD REQUEST TO THE LSQ AND HSQ WITH SECURITY COMPONENT
	public String add(String json, String userid) throws SecurityComponent {
		SecurityComponent security = new SecurityComponent();
		Boolean check = security.CheckStatus();
		if (!check) {
			throw new SecurityComponent("Initialize the Security Component");
		}
		lock.lock();
		synchronized (idreq) {
			Long temp = idreq;
			Long[] resp = new Long[2];
			JParser j = new JParser();
			JsonObject request = j.toJson(json);
			JsonObject high = request.getAsJsonObject("high");
			JsonObject low = request.getAsJsonObject("low");
			String HighMethod = "";
			String LowMethod = "";
			if (high != null)
				HighMethod = high.get("package").getAsString();
			if (low != null)
				LowMethod = low.get("package").getAsString();
			if (!HighMethod.equals("")) {
				high.addProperty("req_id", idreq);
				high.addProperty("user_id", userid);
				HighPriority.add(high);
				resp[0] = (long) 0;
				resp[1] = temp;
				try {
					semaphore2.acquire();
					idreq++;
					lock.unlock();
					return getHighResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!LowMethod.equals("")) {
				low.addProperty("req_id", idreq);
				low.addProperty("user_id", userid);
				LowPriority.add(low);
				resp[0] = (long) 0;
				resp[1] = temp;
				try {
					semaphore.acquire();
					idreq++;
					lock.unlock();
					return getLowResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			lock.unlock();
			return null;
		}
	}

	// WEBSOCKET ADD
	public String add(String json, Session p) throws IOException, SecurityComponent {
		// system.out.println("ADD: " + json);
		SecurityComponent security = new SecurityComponent();
		Boolean check = security.CheckStatus();
		if (check) {
			throw new SecurityComponent("Deactivate the Security Component");
		}
		lock.lock();
		synchronized (WSidreq) {
			Long temp = WSidreq;
			Object[] resp = new Object[4];
			JParser j = new JParser();
			JsonObject request = j.toJson(json);
			JsonObject high = request.getAsJsonObject("high");
			JsonObject low = request.getAsJsonObject("low");
			String HighMethod = "";
			String LowMethod = "";
			if (high != null)
				HighMethod = high.get("package").getAsString();
			if (low != null)
				LowMethod = low.get("package").getAsString();
			if (!HighMethod.equals("")) {
				high.addProperty("req_id", WSidreq);
				high.addProperty("user_id", "1");
				HighPriority.add(high);
				Vector<Session> peer = new Vector<Session>();
				peer.add(p);
				resp[0] = (long) 0;
				resp[1] = temp;
				resp[2] = peer;
				resp[3] = "H";
				try {
					semaphore2.acquire();
					WSidreq++;
					lock.unlock();
					// return resp;
					return getWSHighResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!LowMethod.equals("")) {
				low.addProperty("req_id", WSidreq);
				low.addProperty("user_id", "1");
				LowPriority.add(low);
				Vector<Session> peer = new Vector<Session>();
				peer.add(p);
				resp[0] = (long) 0;
				resp[1] = temp;
				resp[2] = peer;
				resp[3] = "L";
				try {
					semaphore.acquire();
					WSidreq++;
					lock.unlock();
					// return resp;
					return getWSLowResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		lock.unlock();
		return null;

	}

	// WEBSOCKET ADD
	public String add(String json, Vector<Session> p) throws IOException, SecurityComponent {
		// system.out.println("ADD: " + json);
		SecurityComponent security = new SecurityComponent();
		Boolean check = security.CheckStatus();
		if (check) {
			throw new SecurityComponent("Deactivate the Security Component");
		}
		lock.lock();
		synchronized (WSidreq) {
			Long temp = WSidreq;
			Object[] resp = new Object[4];
			JParser j = new JParser();
			JsonObject request = j.toJson(json);
			JsonObject high = request.getAsJsonObject("high");
			JsonObject low = request.getAsJsonObject("low");
			String HighMethod = "";
			String LowMethod = "";
			if (high != null)
				HighMethod = high.get("package").getAsString();
			if (low != null)
				LowMethod = low.get("package").getAsString();
			if (!HighMethod.equals("")) {
				high.addProperty("req_id", WSidreq);
				high.addProperty("user_id", "1");
				HighPriority.add(high);
				resp[0] = (long) 0;
				resp[1] = temp;
				resp[2] = p;
				resp[3] = "H";
				try {
					semaphore2.acquire();
					WSidreq++;
					lock.unlock();
					// return resp;
					return getWSHighResponse(resp);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!LowMethod.equals("")) {
				low.addProperty("req_id", WSidreq);
				low.addProperty("user_id", "1");
				LowPriority.add(low);
				resp[0] = (long) 0;
				resp[1] = temp;
				resp[2] = p;
				resp[3] = "L";
				try {
					semaphore.acquire();
					WSidreq++;
					lock.unlock();
					// return resp;
					return getWSLowResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		lock.unlock();
		return null;

	}

	// ADD REQUEST TO THE LSQ AND HSQ WITHOUT SECURITY COMPONENT
	public String add(String json) throws SecurityComponent {
		// system.out.println("ADD: " + json);
		SecurityComponent security = new SecurityComponent();
		Boolean check = security.CheckStatus();
		if (check) {
			throw new SecurityComponent("Deactivate the Security Component");
		}
		lock.lock();
		synchronized (idreq) {
			Long temp = idreq;
			Long[] resp = new Long[2];
			JParser j = new JParser();
			JsonObject request = j.toJson(json);
			JsonObject high = request.getAsJsonObject("high");
			JsonObject low = request.getAsJsonObject("low");
			String HighMethod = "";
			String LowMethod = "";
			if (high != null)
				HighMethod = high.get("package").getAsString();
			if (low != null)
				LowMethod = low.get("package").getAsString();
			if (!HighMethod.equals("")) {
				high.addProperty("req_id", idreq);
				high.addProperty("user_id", "1");
				HighPriority.add(high);
				resp[0] = (long) 0;
				resp[1] = temp;
				try {
					semaphore2.acquire();
					idreq++;
					lock.unlock();
					return getHighResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!LowMethod.equals("")) {
				low.addProperty("req_id", idreq);
				low.addProperty("user_id", "1");
				LowPriority.add(low);
				resp[0] = (long) 0;
				resp[1] = temp;
				try {
					semaphore.acquire();
					idreq++;
					lock.unlock();
					return getLowResponse(resp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			lock.unlock();
			return null;
		}
	}

	// THE addtoHST METHOD ADD THE HSQ LAST REQUEST INTO THE HST BLOCKINGQUEUE
	public synchronized void addtoHST(JsonObject request) {
		// system.out.println("HST ADDED LOW: "+ request);
		blockingQueueHST.add(new Runnable() {
			@Override
			public void run() {
				// system.out.println("RUNNING HIGH: "+ request);
				Boolean ws = false;
				String callback = null;
				if (request.has("callback")) {
					callback = request.get("callback").getAsString();
					ws = true;
				}
				JsonArray params = request.getAsJsonArray("data");
				String className = request.get("package").getAsString();
				String methName = request.get("method").getAsString();
				methName = methName.replaceAll(" ", "");
				String[] methName1 = (methName.trim()).split("\\(");
				methName = methName1[0];
				methName1[1] = methName1[1].replaceAll("\\)", "");
				methName1[1] = methName1[1].replaceAll("\\[", "");
				methName1[1] = methName1[1].replaceAll("\\]", "");
				String[] struct = methName1[1].split(",");
				String iduser = request.get("user_id").getAsString();
				if (params.size() == 0) {

					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class noparams[] = {};
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, noparams);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, null);
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							HighResponses.add(r);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						HighResponses.add(r);
					}
				}

				if (params.size() == 1) {
					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class[] param = new Class[1]; // 1 length
						JParser parser = new JParser();
						String str = struct[0];
						HashMap dataParam;
						if (params.get(0).isJsonArray()) {
							JsonArray je = params.get(0).getAsJsonArray();
							dataParam = parser.parseParams(je, str);
						} else {
							JsonPrimitive je = params.get(0).getAsJsonPrimitive();
							dataParam = parser.parseParams(je, str);
						}
						param[0] = (Class) dataParam.get("paramclass"); // Integer.class
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, param);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("Status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, dataParam.get("paramvalue"));
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							HighResponses.add(r);
						} catch (Exception ex) {
							ex.printStackTrace();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						HighResponses.add(r);
					}
				}

				if (params.size() > 1) {
					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class<?>[] param = new Class<?>[params.size()];
						Object[] arguments = new Object[params.size()];
						for (int i = 0; i < params.size(); i++) {
							JParser parser = new JParser();
							String str = struct[i];
							HashMap dataParam = null;
							if (params.get(i).isJsonArray()) {
								JsonArray par = params.get(i).getAsJsonArray();
								dataParam = parser.parseParams(par, str);
								System.out.println(dataParam.get("paramvalue"));
							}
							if (params.get(i).isJsonPrimitive()) {
								JsonPrimitive par = params.get(i).getAsJsonPrimitive();
								dataParam = parser.parseParams(par, str);
							}
							param[i] = (Class) dataParam.get("paramclass");
							arguments[i] = dataParam.get("paramvalue");
						}
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, param);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("Status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, arguments);
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							HighResponses.add(r);

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						HighResponses.add(r);
					}

				}
			}
		});

	}

	// THE addtoLST METHOD ADD THE LSQ LAST REQUEST INTO THE LST BLOCKINGQUEUE
	public synchronized void addtoLST(JsonObject request) {
		// system.out.println("LST ADDED LOW: "+ request);
		blockingQueueLST.add(new Runnable() {
			@Override
			public void run() {
				// system.out.println("RUNNING LOW: "+ request);
				Boolean ws = false;
				String callback = null;
				if (request.has("callback")) {
					callback = request.get("callback").getAsString();
					ws = true;
				}
				JsonArray params = request.getAsJsonArray("data");
				String className = request.get("package").getAsString();
				String methName = request.get("method").getAsString();
				methName = methName.replaceAll(" ", "");
				String[] methName1 = (methName.trim()).split("\\(");
				methName = methName1[0];
				methName1[1] = methName1[1].replaceAll("\\)", "");
				methName1[1] = methName1[1].replaceAll("\\[", "");
				methName1[1] = methName1[1].replaceAll("\\]", "");
				String[] struct = methName1[1].split(",");
				String iduser = request.get("user_id").getAsString();
				if (params.size() == 0) {

					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class noparams[] = {};
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, noparams);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("Status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, null);
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							LowResponses.add(r);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						LowResponses.add(r);
					}
				}

				if (params.size() == 1) {
					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class[] param = new Class[1]; // 1 length
						JParser parser = new JParser();
						String str = struct[0];
						HashMap dataParam;
						if (params.get(0).isJsonArray()) {
							JsonArray je = params.get(0).getAsJsonArray();
							dataParam = parser.parseParams(je, str);
						} else {
							JsonPrimitive je = params.get(0).getAsJsonPrimitive();
							dataParam = parser.parseParams(je, str);
						}
						param[0] = (Class) dataParam.get("paramclass"); // Integer.class
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, param);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("Status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, dataParam.get("paramvalue"));
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							LowResponses.add(r);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						LowResponses.add(r);
					}
				}

				if (params.size() > 1) {
					SecurityComponent check = new SecurityComponent();
					Object[] security = check.CheckSecurityData(iduser, className, methName);
					if ((boolean) security[1]) {
						// Setup reflection call
						Class<?>[] param = new Class<?>[params.size()];
						Object[] arguments = new Object[params.size()];
						for (int i = 0; i < params.size(); i++) {
							JParser parser = new JParser();
							String str = struct[i];
							HashMap dataParam = null;
							if (params.get(i).isJsonArray()) {
								JsonArray par = params.get(i).getAsJsonArray();
								dataParam = parser.parseParams(par, str);
							}
							if (params.get(i).isJsonPrimitive()) {
								JsonPrimitive par = params.get(i).getAsJsonPrimitive();
								dataParam = parser.parseParams(par, str);
							}
							param[i] = (Class) dataParam.get("paramclass");
							arguments[i] = dataParam.get("paramvalue");
						}
						try {
							// load the AppTest at runtime
							Class cls = Class.forName(className);
							Object obj = cls.newInstance();
							// call the method
							Method method = cls.getDeclaredMethod(methName, param);
							MetrycBuilder jb = new MetrycBuilder();
							if (ws) {
								jb.add("callback", callback);
							}
							jb.add("Status", true);
							jb.add("id", request.get("req_id").getAsLong());
							String x = (String) method.invoke(obj, arguments);
							JsonElement jelement = new JsonParser().parse(x);
							if(jelement.isJsonObject()){
								JsonObject jobject = jelement.getAsJsonObject();
								jb.add("response", jobject);
							}else{
								jb.add("response", x);
							}
							Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
							LowResponses.add(r);

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						MetrycBuilder jb = new MetrycBuilder();
						jb.add("status", false);
						jb.add("id", request.get("req_id").getAsLong());
						jb.add("response", (String) security[0]);
						Object[] r = { jb.getMetrycBuilder(), request.get("req_id").getAsLong() };
						LowResponses.add(r);
					}
				}
			}
		});

	}

}