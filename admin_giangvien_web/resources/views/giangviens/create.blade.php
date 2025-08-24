@extends('layouts.app')

@section('content')
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm" style="background-color: #ccf5f5; border-radius:10px;">
                <div class="card-body">
                    <h4 class="text-center mb-4 fw-bold">Thêm tài khoản giảng viên</h4>
                    
                    <form action="{{ route('students.store') }}" method="POST">
                        @csrf
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Mã giảng viên</label>
                                <input type="text" name="student_code" class="form-control" placeholder="Mã GV" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Khoa</label>
                                <input type="text" name="class" class="form-control" placeholder="Khoa" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Họ và tên</label>
                            <input type="text" name="fullname" class="form-control" placeholder="Họ tên" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Tên tài khoản</label>
                            <input type="text" name="username" class="form-control" placeholder="tài khoản" required>
                        </div>

                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-success me-2">Tạo tài khoản</button>
                            <a href="{{ route('adminhomes.taikhoansv') }}" class="btn btn-outline-secondary">Huỷ</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
