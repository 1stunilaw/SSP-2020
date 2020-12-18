package ssp.marketplace.app.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.customer.CustomerDetails;
import ssp.marketplace.app.entity.statuses.CommentAccessLevel;
import ssp.marketplace.app.entity.supplier.SupplierDetails;
import ssp.marketplace.app.entity.user.User;

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

    public List<ResponseCommentDto> createDtoFromPublicComments(List<Comment> comments, UUID userId)
    {
        List<ResponseCommentDto> result = new ArrayList<>();

        for (Comment comment : comments)
        {
            result.add(userId == comment.getUser().getId()?createDtoFromComment(comment)
                    :createDtoFromPublicComment(comment));
        }

        return result;
    }

    public ResponseCommentDto createDtoFromComment(Comment question) {
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();

        List<Comment> answers = question.getAnswers();
        if(answers.isEmpty()) {
            CommentDto dto = mapper.commentToCommentDto(question);
            User user = question.getUser();
            dto.setUserName(getName(user));
            responseCommentDto.setQuestion(dto);
        }
        else {
            CommentDto commentDto = mapper.commentToCommentDto(question);
            commentDto.setUserName(getName(question.getUser()));
            responseCommentDto.setQuestion(commentDto);
            ArrayList<CommentDto> answersDto = new ArrayList<CommentDto>();


            for (Comment answer : answers) {
                CommentDto dto = mapper.commentToCommentDto(answer);
                User user = answer.getUser();
                dto.setUserName(getName(user));

                answersDto.add(dto);
            }


            responseCommentDto.setAnswers(answersDto);
        }

        responseCommentDto.setCreationDate(question.getCreatedAt().toString());
        return responseCommentDto;
    }

    public ResponseCommentDto createDtoFromPublicComment(Comment comment) {
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();
        CommentDto question = mapper.commentToCommentDto(comment);
        User user = comment.getUser();
        question.setUserName(getName(user));
        responseCommentDto.setQuestion(question);
        List<Comment> answers = comment.getAnswers();
        List<CommentDto> answersList = new ArrayList<>();


        for (Comment answer : answers) {
            if (answer.getAccessLevel() == CommentAccessLevel.PUBLIC) {
                CommentDto commentDto = mapper.commentToCommentDto(answer);
                User answerUser = answer.getUser();
                commentDto.setUserName(getName(answerUser));
                answersList.add(commentDto);

            }
        }
        responseCommentDto.setAnswers(answersList);
        responseCommentDto.setCreationDate(question.getCreatedAt().toString());
        return responseCommentDto;
    }

    public String getName(User user) {
        CustomerDetails details = user.getCustomerDetails();
        if(details == null) {
            SupplierDetails details1 = user.getSupplierDetails();
            if(details1 == null) return null;
            return  details1.getCompanyName();
        }
        return details.getFio();

    }
}