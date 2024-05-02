package com.retail.e_com.response.dto;

import com.retail.e_com.enums.PriorityRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateContact {

	private int contactId;
	private String name;
	private String email;
	private long phoneNumber;
	private PriorityRole priority;
}
