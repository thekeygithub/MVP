package com.xczg.blockchain.yibaodapp.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 多线程业务处理调度器<br>
 * 说明：
 *     用于执行业务任务单元,由线程池负责调配<br>
 *   
 * @author pepsi.liao
 */
public class ExecutorManager{
	private  ExecutorService execService =null;//业务线程核心处理器
	private static ExecutorManager instance = new ExecutorManager();
	
	private ExecutorManager(){
		execService = Executors.newFixedThreadPool(400, Executors.defaultThreadFactory());
	}
	
	public static  ExecutorManager getInstance(){
			return instance;
	}
	/**
	 * 异步执行第三方接口
	 * @param msg
	 * @param svcNode
	 */
	 public void doExecute(BussinessRunner runner){
			execService.execute(runner);//new BussinessRunner(msg, svcNode)
	 }
	 
	 public void doExecute2(BussinessRunner2 runner){
			execService.execute(runner);//new BussinessRunner(msg, svcNode)
	 }
	
}
