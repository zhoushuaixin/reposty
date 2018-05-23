package cn.itcast.bos.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.PromotionService;
import cn.itcast.bos.service.WayBillService;

public class BosJob implements Job{
	
	@Autowired
	PromotionService promotionService;
	
	@Autowired
	WayBillService wayBillService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 任务一：根据当前时间，判断宣传活动的结束时间是否小于当前时间，更新宣传活动的状态，从1（进行中）变成2（已经结束）
		promotionService.updateStatus(new Date());
		// 任务二：保证数据库的数据和索引库的数据同步
		wayBillService.syncIndex();
	}

	
}
