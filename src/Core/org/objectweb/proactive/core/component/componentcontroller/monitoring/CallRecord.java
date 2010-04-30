package org.objectweb.proactive.core.component.componentcontroller.monitoring;

import java.io.Serializable;

/**
 * Stores the data and timestamps related to a call generated by a request
 * 
 * @author cruz
 *
 */
public class CallRecord extends AbstractRecord implements Serializable {
	
	/** ID of the parent request */
	private ComponentRequestID parentID;
	
	private String calledComponent;
	private String interfaceName;
	private String methodName;
	
	private long sentTime;
	private long replyReceptionTime;
	private long wbnStartTime;
	
	private boolean finished;
	private boolean voidRequest;
	
	public CallRecord() {
	}

	public CallRecord(ComponentRequestID requestID, ComponentRequestID parentID, String calledComponent, String interfaceName, String methodName,
			long sentTime, boolean voidRequest) {
		super(RecordType.CallRecord, requestID);
		this.parentID = parentID;
		this.calledComponent = calledComponent;
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.sentTime = sentTime;
		this.finished = false;
		this.voidRequest = voidRequest;
	}

	public long getSentTime() {
		return sentTime;
	}

	public void setSentTime(long sentTime) {
		this.sentTime = sentTime;
	}

	public long getReplyReceptionTime() {
		return replyReceptionTime;
	}

	public void setReplyReceptionTime(long replyReceptionTime) {
		this.replyReceptionTime = replyReceptionTime;
	}

	public long getWbnStartTime() {
		return wbnStartTime;
	}

	public void setWbnStartTime(long wbnStartTime) {
		this.wbnStartTime = wbnStartTime;
	}

	public ComponentRequestID getParentID() {
		return parentID;
	}

	public String getCalledComponent() {
		return calledComponent;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isVoidRequest() {
		return voidRequest;
	}
	
	
	

}
