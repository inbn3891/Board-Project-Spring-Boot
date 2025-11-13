-- 'board' 테이블 생성 스크립트
-- 프로젝트 실행 시 H2(테스트 환경) 또는 MySQL(실제 환경)에서 사용됩니다.

-- 테이블이 이미 존재하면 삭제
DROP TABLE IF EXISTS board;

-- 'board' 테이블 정의
CREATE TABLE board (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게시글 고유 ID',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    writer VARCHAR(30) NOT NULL COMMENT '작성자 이름',
    hit INT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_date DATETIME NOT NULL COMMENT '생성 일자',
    modified_date DATETIME NULL COMMENT '최종 수정 일자',
    
    PRIMARY KEY (id)
);