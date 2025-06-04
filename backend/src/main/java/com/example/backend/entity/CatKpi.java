package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cat_kpi", schema = "nocpro_common")
public class CatKpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "kpi_name")
    private String kpiName;

    @Column(name = "kpi_group_id")
    private Integer kpiGroupId;

    private String description;

    private Short status;

    @CreationTimestamp
    @Column(name = "insert_time", updatable = false)
    private LocalDateTime insertTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "user_update")
    private String userUpdate;

    @Column(name = "table_name")
    private String tableName;
}
