package com.project.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import com.project.entities.Contact;
import com.project.entities.MyOrder;
import com.project.entities.User;
import com.project.helper.Message;
import com.project.repo.ContactRepository;
import com.project.repo.MyOrderRepository;
import com.project.repo.UserRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final ContactRepository contactRepository;

    private final MyOrderRepository myOrderRepository;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, ContactRepository contactRepository, MyOrderRepository myOrderRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.myOrderRepository = myOrderRepository;
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();

        User user = this.userRepository.getUserByUserName(userName);
        log.info("User {} ", user);

        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    public String dashboardHandler() {
        return "normal/user_dashboard";
    }

    // Add Contacts
    @GetMapping("add-contact")
    public String openAddContactHandler(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContactHandler(@ModelAttribute("contact") Contact contact,
                                        @RequestParam("profileImages") MultipartFile file, Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            User users = this.userRepository.getUserByUserName(name);

            contact.setUser(users);
            log.info("users {}", users);
            if (file.isEmpty()) {
                contact.setImage("contact.png");
            } else {
                // file the file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());

                File savefile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            users.getContacts().add(contact);
            this.userRepository.save(users);

            session.setAttribute("Message", new Message("Your contact is added !!", "success"));

        } catch (Exception e) {
            log.error(e.getMessage());

            session.setAttribute("Message", new Message("Something went to wrong!! Try again...", "danger"));
        }

        return "normal/add_contact_form";
    }

    // /page = 5 , start page = 0
    @GetMapping("/show-contacts/{page}")
    public String showContactHandler(@PathVariable("page") Integer page, Model model, Principal principal) {
        String userName = principal.getName();
        User users = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page, 3);

        Page<Contact> contact = this.contactRepository.getContactsByUser(users.getId(), pageable);

        model.addAttribute("contact", contact);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contact.getTotalPages());

        return "normal/show_contacts";
    }

    // Show contact details
    @GetMapping("/{cId}/contact")
    public String showContactDetailHandler(@PathVariable("cId") Integer cId, Model model, Principal principal) {
        List<Contact> findAll = this.contactRepository.findAll();
        Iterator<Contact> iterator = findAll.iterator();

        Contact contact = null;
        while (iterator.hasNext()) {
            Contact next = iterator.next();
            if (next.getcId() == cId)
                contact = next;
        }

        String userName = principal.getName();
        User users = this.userRepository.getUserByUserName(userName);

        assert contact != null;
        if (users.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "normal/contact_detail";
    }


    @Transactional
    @GetMapping("/delete/{cId}")
    public String deleteContactHandler(@PathVariable("cId") Integer cId, Principal principal, HttpSession session) {
        Contact contact = this.contactRepository.findById(cId).get();

        String userName = principal.getName();
        User users = this.userRepository.getUserByUserName(userName);

        users.getContacts().remove(contact);

        this.userRepository.save(users);

        session.setAttribute("Message", new Message("Contact deleted Successfully...", "success"));

        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/update-contact/{cId}")
    public String updateform(@PathVariable("cId") Integer cId, Model model) {
        model.addAttribute("title", "Update contact");

        Contact contact = this.contactRepository.findById(cId).get();
        model.addAttribute("contact", contact);

        return "normal/update_form";
    }

    // Process Update Contact handler
    @PostMapping("/process-update")
    public String updateProcessHandler(@ModelAttribute Contact contact,
                                       @RequestParam("profileImages") MultipartFile file, Model model, HttpSession session, Principal principal) {
        try {
            // Old contact details
            Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

            if (!file.isEmpty()) {
                // rewrite

                // Delete Old Photo
                File deletefile = new ClassPathResource("static/image").getFile();
                File file1 = new File(deletefile, oldContactDetail.getImage());
                file1.delete();

                // Update with New Photo
                File savefile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                contact.setImage(file.getOriginalFilename());
            } else {
                contact.setImage(oldContactDetail.getImage());
            }

            User users = this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(users);
            this.contactRepository.save(contact);

            session.setAttribute("Message", new Message("Your contact is updated...", "success"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }


        return "redirect:/user/" + contact.getcId() + "/contact";
    }

    @GetMapping("/profile")
    public String profileHandler(Model model) {
        model.addAttribute("title", "Profile");
        return "normal/profile";
    }

    // Open Setting Handler
    @GetMapping("/settings")
    public String settingHandler() {
        return "normal/setting";
    }

    @PostMapping("/change-password")
    public String changePasswordHandler(@RequestParam("oldPassword") String oldPassword,
                                        @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
        System.out.println("OLD PASSWORD : " + oldPassword);
        System.out.println("NEW PASSWORD : " + newPassword);

        String userName = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(userName);
        System.out.println(currentUser.getPassword());

        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
            // Change the Password
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);

            session.setAttribute("Message", new Message("Your Password is changed successfully...", "success"));
        } else {
            session.setAttribute("Message", new Message("Please enter your correct old password !!", "danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

    // creating order for payment
    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws RazorpayException {

        int amt = Integer.parseInt((String) data.get("amount"));

        RazorpayClient client = new RazorpayClient("rzp_test_Iw3y0w0aj23wxf", "MB77O4XNuccsalnLCq5g0A4t");

        JSONObject ob = new JSONObject();
        ob.put("amount", amt * 100);
        ob.put("currency", "INR");
        ob.put("receipt", "txn_235425");

        // creating new order
        Order order = client.orders.create(ob);
        log.info(order.toString());

        // save the order in database
        MyOrder myOrder = new MyOrder();
        myOrder.setAmount(order.get("amount"));
        myOrder.setOrderId(order.get("id"));
        myOrder.setPaymentId("11");
        myOrder.setStatus("created");
        myOrder.setUser(this.userRepository.getUserByUserName(principal.getName()));
        myOrder.setReceipt(order.get("receipt"));

        log.info("order {}", myOrder);
        this.myOrderRepository.save(myOrder);

        // if you want you can save this to your data...
        return order.toString();
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
        MyOrder myOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());
        myOrder.setPaymentId(data.get("payment_id").toString());
        myOrder.setStatus(data.get("status").toString());

        this.myOrderRepository.save(myOrder);

        Map<Object, Object> map = new HashMap<>();
        map.put("msg", "updated");

        return ResponseEntity.ok(map);
    }

}
