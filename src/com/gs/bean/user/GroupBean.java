package com.gs.bean.user;

import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.user.Group;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 9/4/13
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupBean {
    private String groupId = Constants.EMPTY;
    private String groupName = Constants.EMPTY;
    private String groupDescription = Constants.EMPTY;
    private Constants.GROUP_NAME group;

    public GroupBean() {}
    public GroupBean(HashMap<String,String> hashMap) {
        this.groupId = ParseUtil.checkNull(hashMap.get("GROUPID"));
        this.groupName = ParseUtil.checkNull(hashMap.get("GROUP_NAME"));
        this.groupDescription = ParseUtil.checkNull(hashMap.get("GROUP_DESCRIPTION"));
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Constants.GROUP_NAME getGroup() {
        return group;
    }

    public void setGroup(Constants.GROUP_NAME group) {
        this.group = group;
    }


    @Override
    public String toString() {
        return "GroupBean{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", group=" + group +
                '}';
    }

}
