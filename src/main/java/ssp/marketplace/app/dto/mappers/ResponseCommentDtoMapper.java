package ssp.marketplace.app.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ssp.marketplace.app.dto.responseDto.ResponseOneCommentDto;
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
            result.add(userId.equals(comment.getUser().getId()) ?createDtoFromComment(comment)
                    :createDtoFromPublicComment(comment));
        }

        return result;
    }

    public ResponseCommentDto createDtoFromComment(Comment question) {
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();

        List<Comment> answers = question.getAnswers();
        if(answers.isEmpty()) {
            ResponseOneCommentDto dto = mapper.commentToCommentDto(question);
            User user = question.getUser();
            dto.setUserName(getName(user));
            responseCommentDto.setQuestion(dto);
        }
        else {
            ResponseOneCommentDto responseOneCommentDto = mapper.commentToCommentDto(question);
            responseOneCommentDto.setUserName(getName(question.getUser()));
            responseCommentDto.setQuestion(responseOneCommentDto);
            ArrayList<ResponseOneCommentDto> answersDto = new ArrayList<ResponseOneCommentDto>();


            for (Comment answer : answers) {
                ResponseOneCommentDto dto = mapper.commentToCommentDto(answer);
                User user = answer.getUser();
                dto.setUserName(getName(user));

                answersDto.add(dto);
            }


            responseCommentDto.setAnswers(answersDto);
        }

        return responseCommentDto;
    }

    public ResponseCommentDto createDtoFromPublicComment(Comment comment) {
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();
        ResponseOneCommentDto question = mapper.commentToCommentDto(comment);
        User user = comment.getUser();
        question.setUserName(getName(user));
        responseCommentDto.setQuestion(question);
        List<Comment> answers = comment.getAnswers();
        List<ResponseOneCommentDto> answersList = new ArrayList<>();


        for (Comment answer : answers) {
            if (answer.getAccessLevel() == CommentAccessLevel.PUBLIC) {
                ResponseOneCommentDto responseOneCommentDto = mapper.commentToCommentDto(answer);
                User answerUser = answer.getUser();
                responseOneCommentDto.setUserName(getName(answerUser));
                answersList.add(responseOneCommentDto);

            }
        }
        responseCommentDto.setAnswers(answersList);
        return responseCommentDto;
    }

    public String getName(User user) {
        CustomerDetails details = user.getCustomerDetails();
        if(details == null) {
            SupplierDetails details1 = user.getSupplierDetails();
            if(details1 == null) {
                return null;
            }
            return  details1.getCompanyName();
        }
        return details.getFio();

    }
}