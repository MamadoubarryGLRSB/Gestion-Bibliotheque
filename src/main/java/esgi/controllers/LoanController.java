package esgi.controllers;

import esgi.models.Loan;
import esgi.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam int userId, @RequestParam Long bookId) {
        Loan loan = loanService.createLoanRequest(userId, bookId);
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/{loanId}/approve")
    public ResponseEntity<Loan> approveLoan(@PathVariable Long loanId) {
        Loan loan = loanService.approveLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long loanId) {
        Loan loan = loanService.returnLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getUserLoans(@RequestParam Long userId) {
        List<Loan> loans = loanService.getUserLoans(userId);
        return ResponseEntity.ok(loans);
    }
}
