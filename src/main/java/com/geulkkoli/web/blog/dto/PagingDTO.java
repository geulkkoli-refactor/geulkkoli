package com.geulkkoli.web.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class PagingDTO {

    private List<ArticlePagingRequestDTO> list;

    private int number;

    private boolean first;

    private boolean last;

    private int totalPages;

    private int size;

    public PagingDTO(List<ArticlePagingRequestDTO> list, int number, boolean first, boolean last, int totalPages, int size) {
        this.list = list;
        this.number = number;
        this.first = first;
        this.last = last;
        this.totalPages = totalPages;
        this.size = size;
    }

    public static PagingDTO listDTOtoPagingDTO (Page<ArticlePagingRequestDTO> page) {
        return new PagingDTO(
                page.toList(),
                page.getNumber(),
                page.isFirst(),
                page.isLast(),
                page.getTotalPages(),
                page.getSize()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagingDTO)) return false;
        PagingDTO pagingDTO = (PagingDTO) o;
        return number == pagingDTO.number && first == pagingDTO.first && last == pagingDTO.last && totalPages == pagingDTO.totalPages && size == pagingDTO.size && Objects.equals(list, pagingDTO.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list, number, first, last, totalPages, size);
    }
}
