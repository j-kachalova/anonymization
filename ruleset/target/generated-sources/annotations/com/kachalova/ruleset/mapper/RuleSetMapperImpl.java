package com.kachalova.ruleset.mapper;

import com.kachalova.ruleset.dto.AnonymizationRuleDto;
import com.kachalova.ruleset.dto.RuleSetRequestDto;
import com.kachalova.ruleset.dto.RuleSetResponseDto;
import com.kachalova.ruleset.entity.AnonymizationMethod;
import com.kachalova.ruleset.entity.AnonymizationRuleEntity;
import com.kachalova.ruleset.entity.RuleSetEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-18T05:00:03+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class RuleSetMapperImpl implements RuleSetMapper {

    @Override
    public RuleSetEntity toEntity(RuleSetRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        RuleSetEntity.RuleSetEntityBuilder ruleSetEntity = RuleSetEntity.builder();

        ruleSetEntity.name( dto.getName() );
        ruleSetEntity.description( dto.getDescription() );
        ruleSetEntity.rules( anonymizationRuleDtoListToAnonymizationRuleEntityList( dto.getRules() ) );

        return ruleSetEntity.build();
    }

    @Override
    public AnonymizationRuleEntity toEntity(AnonymizationRuleDto dto) {
        if ( dto == null ) {
            return null;
        }

        AnonymizationRuleEntity.AnonymizationRuleEntityBuilder anonymizationRuleEntity = AnonymizationRuleEntity.builder();

        anonymizationRuleEntity.id( dto.getId() );
        anonymizationRuleEntity.fieldName( dto.getFieldName() );
        if ( dto.getMethod() != null ) {
            anonymizationRuleEntity.method( Enum.valueOf( AnonymizationMethod.class, dto.getMethod() ) );
        }
        anonymizationRuleEntity.parameters( dto.getParameters() );

        return anonymizationRuleEntity.build();
    }

    @Override
    public AnonymizationRuleDto toDto(AnonymizationRuleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AnonymizationRuleDto.AnonymizationRuleDtoBuilder anonymizationRuleDto = AnonymizationRuleDto.builder();

        anonymizationRuleDto.id( entity.getId() );
        anonymizationRuleDto.fieldName( entity.getFieldName() );
        if ( entity.getMethod() != null ) {
            anonymizationRuleDto.method( entity.getMethod().name() );
        }
        anonymizationRuleDto.parameters( entity.getParameters() );

        return anonymizationRuleDto.build();
    }

    @Override
    public List<RuleSetResponseDto> toResponseDtoList(List<RuleSetEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RuleSetResponseDto> list = new ArrayList<RuleSetResponseDto>( entities.size() );
        for ( RuleSetEntity ruleSetEntity : entities ) {
            list.add( toResponseDto( ruleSetEntity ) );
        }

        return list;
    }

    @Override
    public RuleSetResponseDto toResponseDto(RuleSetEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RuleSetResponseDto.RuleSetResponseDtoBuilder ruleSetResponseDto = RuleSetResponseDto.builder();

        ruleSetResponseDto.id( entity.getId() );
        ruleSetResponseDto.name( entity.getName() );
        ruleSetResponseDto.description( entity.getDescription() );
        ruleSetResponseDto.rules( anonymizationRuleEntityListToAnonymizationRuleDtoList( entity.getRules() ) );

        return ruleSetResponseDto.build();
    }

    protected List<AnonymizationRuleEntity> anonymizationRuleDtoListToAnonymizationRuleEntityList(List<AnonymizationRuleDto> list) {
        if ( list == null ) {
            return null;
        }

        List<AnonymizationRuleEntity> list1 = new ArrayList<AnonymizationRuleEntity>( list.size() );
        for ( AnonymizationRuleDto anonymizationRuleDto : list ) {
            list1.add( toEntity( anonymizationRuleDto ) );
        }

        return list1;
    }

    protected List<AnonymizationRuleDto> anonymizationRuleEntityListToAnonymizationRuleDtoList(List<AnonymizationRuleEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<AnonymizationRuleDto> list1 = new ArrayList<AnonymizationRuleDto>( list.size() );
        for ( AnonymizationRuleEntity anonymizationRuleEntity : list ) {
            list1.add( toDto( anonymizationRuleEntity ) );
        }

        return list1;
    }
}
