package com.oxchains.themis.order.common;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author huohuo
 */
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterRequest {

  private MultipartFile multipartFile;
  private String id;
  private Long userId;
  private String content;
  private String fileName;

}
