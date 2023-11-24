package com.binar.byteacademy.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    List<T> content;
    CustomPageable pageable;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(), page.getTotalElements());
    }

    @Data
    static class CustomPageable{
        int pagenumber;
        int pagesize;
        long totalelements;

        public CustomPageable(int pagenumber, int pagesize, long totalelements) {
            this.pagenumber = pagenumber;
            this.pagesize = pagesize;
            this.totalelements = totalelements;
        }
    }
}
