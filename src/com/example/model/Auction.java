package com.example.model;

import java.io.Serializable;

public class Auction implements Serializable
{
	public Commodity commodity;
	public int ID;
	public String deadLine;
	public double activePrice;
	public double currentPrice;
	public String currentMail;
	public String mailOfparticipants;
	
	public Auction()
	{
		this.commodity=new Commodity();
	}
}
