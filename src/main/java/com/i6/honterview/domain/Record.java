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
@Table(name = "record")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Record {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "processing_time", nullable = false)
	private int processingTime;

	public Record(String fileName, int processingTime) {
		this.fileName = fileName;
		this.processingTime = processingTime;
	}

	@Override
	public String toString() {
		return "Record{" +
			"id=" + id +
			", fileName='" + fileName + '\'' +
			", processingTime=" + processingTime +
			'}';
	}
}
