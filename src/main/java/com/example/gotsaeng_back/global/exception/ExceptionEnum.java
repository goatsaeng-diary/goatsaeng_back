package com.example.gotsaeng_back.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    // System Exception
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류 발생"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    DUPLICATE(HttpStatus.CONFLICT,"DUPLICATED" , "중복된 아이디 입니다"),
    DUPLICATEMAIL(HttpStatus.CONFLICT,"DUPLICATEDEMAIL" , "중복된 이메일 입니다"),

    // Token Exception
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "토큰이 만료되었습니다."),
    EXPIRED_TOKEN2(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "토큰 유효성 실패"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "UNSUPPORTED_TOKEN", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOKEN_NOT_FOUND", "토큰을 찾을 수 없습니다."),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, "INVALID_SIGNATURE", "유효하지 않은 서명입니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", "잘못된 인자입니다."),
    JWT_FILTER_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_FILTER_INTERNAL_ERROR", "JWT 필터 내부 오류입니다."),

    // User
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "EXIST_EMAIL", "이미 있는 이메일 입니다."),
    NO_SEARCH_EMAIL(HttpStatus.BAD_REQUEST,"NO_SEARCH_EMAIL","없는 이메일 입니다"),
    FAIL_EMAIL_SEND(HttpStatus.BAD_REQUEST,"FAIL_EMAIL_SEND","이메일 발송 오류"),
    NOT_ALLOW_FILED(HttpStatus.BAD_REQUEST, "NOT_ALLOW_FILED", "이메일 혹은 비밀번호 형식이 틀립니다."),
    DIFFERENT_PASSWORD(HttpStatus.BAD_REQUEST, "DIFFERENT_PASSWORD", "비밀번호가 둘이 다릅니다."),
    FOLLOW_FAIL(HttpStatus.BAD_REQUEST, "FOLLOW_FAIL", "팔로우에 실패하였습니다"),
    UNFOLLOW_FAIL(HttpStatus.BAD_REQUEST, "UNFOLLOW_FAIL", "팔로우에 실패하였습니다"),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "등록된 댓글이 없습니다."),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT_DELETE_FORBIDDEN", "댓글 삭제 권한이 없습니다."),
    COMMENT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT_UPDATE_FORBIDDEN", "댓글 수정 권한이 없습니다."),

    // Reply
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "REPLY_NOT_FOUND", "대댓글을 찾을 수 없습니다."),
    REPLY_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "REPLY_DELETE_FORBIDDEN", "대댓글 삭제 권한이 없습니다."),
    REPLY_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "REPLY_UPDATE_FORBIDDEN", "대댓글 수정 권한이 없습니다."),
    REPLY_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REPLY_CREATE_FAILED", "대댓글 생성 실패");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
