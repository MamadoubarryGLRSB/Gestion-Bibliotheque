package esgi.controllers;

import esgi.models.Loan;
import esgi.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {


    private LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/user/{userId}")
    public List<Loan> getLoansByUser(@PathVariable Integer userId) {
        return loanService.getLoansByUser(userId);
    }

    @GetMapping("/active")
    public List<Loan> getActiveLoans() {
        return loanService.getActiveLoans();
    }

    @PostMapping("/borrow")
    public Loan borrowBook(@RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");

        Long bookId = ((Number) payload.get("bookId")).longValue();
        return loanService.borrowBook(userId, bookId);
    }
    @PutMapping("/validate/{loanId}")
    public Loan validateLoanRequest(
            @PathVariable Long loanId,
            @RequestParam boolean isApproved
    ) {
        return loanService.validateLoanRequest(loanId, isApproved);
    }

    @PutMapping("/return/{loanId}")
    public Loan returnBook(@PathVariable Long loanId) {
        return loanService.returnBook(loanId);
    }

    @DeleteMapping("/delete/{loanId}")
    public void deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
    }
}
