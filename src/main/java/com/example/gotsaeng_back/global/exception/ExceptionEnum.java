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
    DUPLICATE_MAIL(HttpStatus.CONFLICT,"DUPLICATED_EMAIL" , "중복된 이메일 입니다"),

    // Token Exception
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "토큰이 만료되었습니다."),
    EXPIRED_TOKEN2(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "토큰 유효성 실패"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "UNSUPPORTED_TOKEN", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOKEN_NOT_FOUND", "토큰을 찾을 수 없습니다."),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, "INVALID_SIGNATURE", "유효하지 않은 서명입니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", "잘못된 인자입니다."),
    JWT_FILTER_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_FILTER_INTERNAL_ERROR", "JWT 필터 내부 오류입니다."),
    SOCIAL_LOGIN_FAIL(HttpStatus.BAD_REQUEST,"소셜로그인실패"),
    // User
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "EXIST_EMAIL", "이미 있는 이메일 입니다."),
    NO_SEARCH_EMAIL(HttpStatus.BAD_REQUEST,"NO_SEARCH_EMAIL","없는 이메일 입니다"),
    FAIL_EMAIL_SEND(HttpStatus.BAD_REQUEST,"FAIL_EMAIL_SEND","이메일 발송 오류"),
    NOT_ALLOW_FILED(HttpStatus.BAD_REQUEST, "NOT_ALLOW_FILED", "이메일 혹은 비밀번호 형식이 틀립니다."),
    DIFFERENT_PASSWORD(HttpStatus.BAD_REQUEST, "DIFFERENT_PASSWORD", "비밀번호가 둘이 다릅니다."),
    FOLLOW_FAIL(HttpStatus.BAD_REQUEST, "FOLLOW_FAIL", "팔로우에 실패하였습니다."),
    UNFOLLOW_FAIL(HttpStatus.BAD_REQUEST, "UNFOLLOW_FAIL", "팔로우에 실패하였습니다."),
    ID_PASSWORD_FAIL(HttpStatus.BAD_REQUEST,"LOGIN_FAIL","아이디 또는 비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_NOT_FOUND","유저를 찾지 못했습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "등록된 댓글이 없습니다."),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT_DELETE_FORBIDDEN", "댓글 삭제 권한이 없습니다."),
    COMMENT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT_UPDATE_FORBIDDEN", "댓글 수정 권한이 없습니다."),

    // Reply
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "REPLY_NOT_FOUND", "대댓글을 찾을 수 없습니다."),
    REPLY_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "REPLY_DELETE_FORBIDDEN", "대댓글 삭제 권한이 없습니다."),
    REPLY_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "REPLY_UPDATE_FORBIDDEN", "대댓글 수정 권한이 없습니다."),
    REPLY_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REPLY_CREATE_FAILED", "대댓글 생성 실패"),

    // Record
    RECORD_TARGET_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_TARGET_TYPE_NOT_FOUND", "선택된 기록 종류가 없습니다."),
    RECORD_TARGET_TYPE_DUPLICATE(HttpStatus.CONFLICT, "RECORD_TARGET_TYPE_DUPLICATE", "기록 종류가 중복으로 선택되었습니다."),
    RECORD_TARGET_DUPLICATE(HttpStatus.CONFLICT, "RECORD_TARGET_DUPLICATE", "이미 등록된 기록 목표가 있습니다."),
    RECORD_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_TARGET_NOT_FOUND", "기록된 목표가 없습니다."),

    RECORD_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_TYPE_NOT_FOUND", "등록된 기본 기록 종류가 없습니다."),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_NOT_FOUND", "등록된 기록이 없습니다."),
    RECORD_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECORD_UPDATE_FORBIDDEN", "기록 수정 권한이 없습니다."),
    RECORD_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECORD_DELETE_FORBIDDEN", "기록 삭제 권한이 없습니다."),

    CUSTOM_RECORD_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOM_RECORD_TYPE_NOT_FOUND", "등록된 커스텀 기록 종류가 없습니다."),
    CUSTOM_RECORD_TYPE_DUPLICATE(HttpStatus.CONFLICT, "CUSTOM_RECORD_TYPE_DUPLICATE", "이미 등록된 커스텀 기록 종류입니다."),
    CUSTOM_RECORD_TYPE_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "CUSTOM_RECORD_TYPE_UPDATE_FORBIDDEN", "커스텀 기록 종류 수정 권한이 없습니다."),
    CUSTOM_RECORD_TYPE_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "CUSTOM_RECORD_TYPE_DELETE_FORBIDDEN", "커스텀 기록 종류 삭제 권한이 없습니다."),

    CUSTOM_RECORD_DUPLICATE(HttpStatus.CONFLICT, "CUSTOM_RECORD_DUPLICATE", "이미 등록된 커스텀 기록입니다."),
    CUSTOM_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOM_RECORD_NOT_FOUND", "등록된 커스텀 기록이 없습니다."),
    CUSTOM_RECORD_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "CUSTOM_RECORD_UPDATE_FORBIDDEN", "기록 수정 권한이 없습니다."),
    CUSTOM_RECORD_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "CUSTOM_RECORD_DELETE_FORBIDDEN", "기록 삭제 권한이 없습니다."),

    // Post
    EDIT_NOT_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR, "EDIT_NOT_COMPLETED", "게시물을 수정하지 못했습니다."),
    CREATE_NOT_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR,"CREATE_NOT_COMPLETED", "게시물을 작성하지 못했습니다."),
    DELETE_NOT_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR, "DELETE_NOT_COMPLETED", "게시물을 삭제하지 못했습니다."),
    HISTORY_SAVE_NOT_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR, "HISTORY_SAVE_NOT_COMPLETED", "시청기록을 저장하지 못했습니다."),
    VIEW_NOT_INCREASED(HttpStatus.INTERNAL_SERVER_ERROR,"VIEW_NOT_INCREASED","조회수를 수정하지 못했습니다."),

    // S3
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3_UPLOAD_FAIL","S3에 업로드하지 못했습니다."),
    S3_DOWNLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3_DOWNLOAD_FAIL", "S3에서 불러오지 못했습니다."),
    S3_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3_DELETE_FAIL","S3에서 삭제하지 못했습니다.");
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
