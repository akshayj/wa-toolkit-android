package com.windowsazure.samples;

import com.windowsazure.samples.HttpStatusCode;

public enum HttpStatusCode {
	
	// Informational
	Continue(100),
	SwitchingProtocols(101),
	Processing(102),
	RequestUriTooLongInfo(103),	// "Info" appended to not conflict with 414
	
	// Success
	OK(200),
	Created(201),
	Accepted(202),
	NonAuthoritativeInformation(203),
	NoContent(204),
	ResetContent(205),
	PartialContent(206),
	MultiStatus(207),
	ImUsed(226),
	
	// Redirection
	MultipleChoices(300),
	MovedPermantly(301),
	Found(302),
	SeeOther(303),
	NotModified(304),
	UseProxy(305),
	SwitchProxy(306),
	TemporaryRedirect(307),
	
	// Client Error
	BadRequest(400),
	Unauthorized(401),
	PaymentRequired(402),
	Forbidden(403),
	NotFound(404),
	MethodNotAllowed(405),
	NotAcceptable(406),
	ProxyAuthenticationRequired(407),
	RequestTimeout(408),
	Conflict(409),
	Gone(410),
	LengthRequired(411),
	PreconditionFailed(412),
	RequestEntityTooLarge(413),
	RequestUriTooLong(414),
	UnsupportedMediaType(415),
	RequestedRangeNotSatisfiable(416),
	ExpectationFailed(417),
	ImATeapot(418),	// RFC 2324 Hyper Text Coffee Pot Control Protocol (1998 IETF April Fools' Joke)
	UnproccessableEntity(422),
	Locked(423),
	FailedDependency(424),
	UnorderedCollection(425),
	UpgradeRequired(426),
	NoResponse(444),
	RetryWith(449),
	BlockedByWindows(450),
	ClientClosedRequest(499),
	
	// Server Error
	InternalServerError(500),
	NotImplemented(501),
	BadGateway(502),
	ServiceUnavailable(503),
	GatewayTimeout(504),
	HttpVersionNotSupported(505),
	VariantAlsoNegotiates(506),
	InsufficientStorage(507),
	BandwidthLimitExceeded(508),
	NotExtended(510),
	
	InvalidStatusCode(0);
	
	public static HttpStatusCode fromInt(int status)
		throws Exception {
		
		for (HttpStatusCode code : values()) {
			if (code.status == status && code != InvalidStatusCode)
				return code;
		}
		
		throw new Exception("Invalid HttpStatusCode " + status);
	}
	
	public boolean isSuccess() {
		return (status >= 200 && status <= 299);
	}
	
	private HttpStatusCode(int status) {
		this.status = status;
	}
	
	private int status;
}
