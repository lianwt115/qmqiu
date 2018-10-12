package com.lwt.qmqiu.bean

import android.os.SystemClock
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.*
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.MemberPushOption
import com.netease.nimlib.sdk.msg.model.NIMAntiSpamOption

class MyMessage :IMMessage {

    private var content:String?= null

    override fun getFromClientType(): Int {

        return 0
    }

    override fun setRemoteExtension(remoteExtension: MutableMap<String, Any>?) {
    }

    override fun setLocalExtension(localExtension: MutableMap<String, Any>?) {
    }

    override fun getNIMAntiSpamOption(): NIMAntiSpamOption? {
        return null
    }

    override fun getSessionId(): String {
        return ""
    }

    override fun getAttachment(): MsgAttachment? {
        return null
    }

    override fun getTeamMsgAckCount(): Int {

        return 0
    }

    override fun isRemoteRead(): Boolean {

        return true
    }

    override fun setPushPayload(pushPayload: MutableMap<String, Any>?) {

    }

    override fun getPushContent(): String {

        return ""
    }

    override fun isTheSame(message: IMMessage?): Boolean {
        return false
    }

    override fun setConfig(config: CustomMessageConfig?) {
        }

    override fun getContent(): String {

        if (content  == null)
            return ""
        return content!!
    }

    override fun setAttachment(attachment: MsgAttachment?) {
    }

    override fun getPushPayload(): MutableMap<String, Any>? {
        return  null
    }

    override fun getAttachStatus(): AttachStatusEnum? {
        return  null
    }

    override fun getDirect(): MsgDirectionEnum ?{
        return  null
    }

    override fun hasSendAck(): Boolean {
        return false
    }

    override fun getTeamMsgUnAckCount(): Int {
        return 0
    }

    override fun getStatus(): MsgStatusEnum? {
        return null
    }

    override fun setAttachStatus(attachStatus: AttachStatusEnum?) {
    }

    override fun setClientAntiSpam(hit: Boolean) {
    }

    override fun setDirect(direct: MsgDirectionEnum?) {
    }

    override fun setStatus(status: MsgStatusEnum?) {
    }

    override fun setContent(content: String?) {
        this.content = content
    }

    override fun getUuid(): String {
        return "1"
    }

    override fun setNIMAntiSpamOption(nimAntiSpamOption: NIMAntiSpamOption?) {
    }

    override fun getTime(): Long {
        return  System.currentTimeMillis()
    }

    override fun setPushContent(pushContent: String?) {
    }

    override fun setFromAccount(account: String?) {

    }

    override fun getRemoteExtension(): MutableMap<String, Any>? {
        return null
    }


    override fun getSessionType(): SessionTypeEnum {
        return  SessionTypeEnum.P2P
    }

    override fun getMemberPushOption(): MemberPushOption? {

        return null
    }

    override fun getFromAccount(): String {
        return "自己"
    }

    override fun getConfig(): CustomMessageConfig? {

        return  null
    }

    override fun getMsgType(): MsgTypeEnum {
        return MsgTypeEnum.text
    }

    override fun needMsgAck(): Boolean {
        return false
    }

    override fun getFromNick(): String {
        return ""
    }

    override fun setMsgAck() {

    }

    override fun getLocalExtension(): MutableMap<String, Any>? {
        return null
    }

    override fun setMemberPushOption(pushOption: MemberPushOption?) {
    }
}