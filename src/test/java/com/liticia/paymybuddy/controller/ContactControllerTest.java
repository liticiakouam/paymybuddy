package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

}