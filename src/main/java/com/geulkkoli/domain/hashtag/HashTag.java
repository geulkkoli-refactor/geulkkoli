package com.geulkkoli.domain.hashtag;

import com.geulkkoli.domain.hashtag.util.HashTagTypeConverter;
import com.geulkkoli.domain.post.ConfigDate;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "hashtag")
public class HashTag extends ConfigDate {

    @Id @Column(name = "hashTag_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashTagId;

    @Column(nullable = false, name = "hashTag_Name")
    private String hashTagName;

    @Convert(converter = HashTagTypeConverter.class)
    @Column(nullable = false, name = "hashTag_Type")
    private HashTagType hashTagType;

    //PostHashTag 클라스와 일대다로 묶는 메서드
    @OneToMany(mappedBy = "hashTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostHashTag> postHashTags = new LinkedHashSet<>();

    @Builder
    public HashTag (String hashTagName , HashTagType hashTagType){
        this.hashTagName = hashTagName;
        this.hashTagType = hashTagType;
    }


    @Override
    public boolean equals(Object obj) {
        HashTag hashTag = (HashTag) obj;
        return getHashTagId() != null && Objects.equals(getHashTagId(), hashTag.getHashTagId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "HashTag{" +
                "hashTagName='" + hashTagName + '\'' +
                ", hashTagType=" + hashTagType +
                '}';
    }
}
