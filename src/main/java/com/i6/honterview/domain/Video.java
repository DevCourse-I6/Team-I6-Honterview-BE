package com.i6.honterview.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "video")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Video extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "processing_time", nullable = false)
	private int processingTime;

	public Video(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Video{" +
			"id=" + id +
			", fileName='" + fileName + '\'' +
			", processingTime=" + processingTime +
			'}';
	}
}
