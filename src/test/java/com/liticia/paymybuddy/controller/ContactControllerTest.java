package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ContactController.class})
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    public void testShouldReturnOkWhenAddingContact() throws Exception {
        doNothing().when(contactService).save(3);

        mockMvc.perform(get("/addUser/3"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user?pageNumber=1"))
                .andReturn();
    }

    @Test
    public void testShouldThrowUserNotFoundException() throws Exception {
        doThrow(UserNotFoundException.class).when(contactService).save(1);

        mockMvc.perform(get("/addUser/3"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user?pageNumber=1"))
                .andReturn();
    }

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndContactLengthIsCorrect() throws Exception {
        User userF = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anz").build();

        List<Contact> contacts = Arrays.asList(
                Contact.builder().user(user).userFriend(userF).build(),
                Contact.builder().user(user).userFriend(userF).build()
        );
        Page<Contact> page = new PageImpl<>(contacts);

        when(contactService.findPaginated(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/contact?pageNumber=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("contacts"))
                .andReturn();

    }

    @Test
    void testShouldDeleteContact() throws Exception {
        doNothing().when(contactService).removeUser(1L);

        mockMvc.perform(get("/contact/remove/1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/contact?pageNumber=1"))
                .andReturn();
    }

    @Test
    void testShouldFindContactById() throws Exception {
        Contact contact = Contact.builder().id(1).build();
        when(contactService.findById(1L)).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/contact/find/1"))
                .andExpect(status().isFound())
                .andReturn();
    }
}