package ssp.marketplace.app.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.Comment;

import java.util.*;

@Mapper
public class ResponseCommentDtoMapper {
    public List<ResponseCommentDto> createDtoFromComments(List<Comment> comments)
    {
        List<ResponseCommentDto> result = new ArrayList<>();

        for (Comment comment : comments)
        {
            result.add(createDtoFromComment(comment));
        }

        return result;
    }

    public ResponseCommentDto createDtoFromComment(Comment question) {
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();

        List<Comment> answers = question.getAnswers();
        if(answers == null) {
            responseCommentDto.setQuestion(mapper.commentToCommentDto(question));
        }
        else {
            responseCommentDto.setQuestion(mapper.commentToCommentDto(question));
            ArrayList<CommentDto> answersDto = new ArrayList<CommentDto>();

            for (Comment answer : answers) {
                answersDto.add(mapper.commentToCommentDto(answer));
            }

            responseCommentDto.setAnswers(answersDto);
        }

        return responseCommentDto;
    }
}