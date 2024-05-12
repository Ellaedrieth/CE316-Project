    package com.example.ce316project;

    public class Student {

        private String student_id;
        private String result;

        public Student(String result, String student_id) {
            this.result = result;
            this.student_id = student_id;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }



    }


