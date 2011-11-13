package com.gs.json;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Text
{
	protected String text = "";
	protected String textLocationId = "";

	public Text(String text, String textLocationId)
	{
		super();
		this.text = text;
		this.textLocationId = textLocationId;
	}

	public JSONObject toJson() throws JSONException
	{
		JSONObject jsonMessageObject = new JSONObject();

		jsonMessageObject.put(RespConstants.Key.TEXT_LOC_ID.getKey(), textLocationId);
		jsonMessageObject.put(RespConstants.Key.TEXT_LOC_ID.getKey(), text);

		return jsonMessageObject;

	}

}
