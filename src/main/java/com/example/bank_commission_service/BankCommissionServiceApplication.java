package com.example.bank_commission_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
 * نکات اجرای پروژه:
 *
 * 1. پایگاه داده:
 * - این پروژه به صورت پیش‌فرض از
 *  H2
 * استفاده می‌کند
 * - برای تغییر به دیتابیس دیگر، فایل
 * application.properties
 * را ویرایش کنید
 * - دسترسی به کنسول H2: http://localhost:8080/h2-console
 *   - JDBC URL: jdbc:h2:mem:testdb
 *   - Username: sa
 *   - Password: [خالی]
 *
 * 2. داده‌های اولیه:
 * - کلاس DataInitService
 *  برای بارگذاری داده‌های تستی استفاده شده است
 * - این داده‌ها شامل کاربران و اطلاعات نمونه می‌باشد
 * - برای راحتی تست
 * OAuth2
 * می توانید کابربری با ایمیل خود بسازید
 *
 * 3. تست OAuth2:
 * - از endpoint زیر برای تست استفاده کنید:
 *   - http://localhost:8080/login
 *
 */
@SpringBootApplication
public class BankCommissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankCommissionServiceApplication.class, args);
	}

}
