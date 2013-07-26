package com.gs.manager.event;

import com.gs.bean.TelNumberBean;

public class TelNumberResponse
{
	private TelNumberBean telNumberBean = new TelNumberBean();

	public TelNumberBean getTelNumberBean()
	{
		return telNumberBean;
	}

	public void setTelNumberBean(TelNumberBean telNumberBean)
	{
		this.telNumberBean = telNumberBean;
	}

    @Override
    public String toString() {
        return "TelNumberResponse{" +
                "telNumberBean=" + telNumberBean +
                '}';
    }
}
