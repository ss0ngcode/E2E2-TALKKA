package com.talkka.server.api.datagg.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "response")
public record BusRouteInfoRespDto(@JacksonXmlProperty(localName = "comMsgHeader") Map<String, String> comMsgHeader,
								  @JacksonXmlProperty(localName = "msgHeader") Map<String, String> msgHeader,
								  @JacksonXmlProperty(localName = "msgBody") List<BusRouteInfoBodyDto> msgBody)
	implements PublicBusApiResp<BusRouteInfoBodyDto> {
}
