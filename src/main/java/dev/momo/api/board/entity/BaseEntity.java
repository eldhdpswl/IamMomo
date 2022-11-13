package dev.momo.api.board.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false) //누가 수정하지 못하도록 막는역할
    private Instant createAt;

    @LastModifiedDate
    private Instant updateAt;

    protected BaseEntity() {
    }


}
