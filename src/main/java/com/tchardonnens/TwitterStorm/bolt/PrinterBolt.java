package com.tchardonnens.TwitterStorm.bolt;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {
	
	@Override
	public void cleanup() {
		
	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context){
		
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		System.out.println(input.getValue(0));	
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
