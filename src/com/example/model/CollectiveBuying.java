package com.example.model;

import java.io.Serializable;

public class CollectiveBuying implements Serializable
{
	public Commodity commodity;
	public int ID;
	public String deadLine;
	public int activeNum;
	public double collectivePrice;
	public String mailOfparticipants;
	public int currentNum;
	
	public CollectiveBuying()
	{
		this.commodity=new Commodity();
	}
}
