@extends('layouts.app')

@section('content')
<div class="container  mb-5" style="padding-bottom:150px;">
    <div class="row justify-content-center">
        <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header bg-light">
                    <h3 class="mb-0">Bảng điều khiển quản trị viên</h3>
                </div>
                <div class="card-body" style="background-color:#b3d9ff; padding:0;">
                    <div class="row row-cols-5 g-0 text-center">

                        <!-- Tài khoản sinh viên -->
                        <a href="{{route('adminhomes.taikhoansv')}}">
                                   <div class="col">
                            <div class="card h-100 shadow-sm rounded-0">
                                <div class="card-body">
                                    <h6 class="fw-bold">📘 Tài khoản sinh viên</h6>
                                    <p class="text-muted small mb-0">
                                        Quản lý danh sách sinh viên, thông tin đăng nhập, trạng thái
                                    </p>
                                    
                                </div>
                            </div>
                        </div>

                        </a>
                 

                        <!-- Tài khoản giảng viên -->
                        <a href="{{route('adminhomes.taikhoangv')}}">
                             <div class="col">
                            <div class="card h-100 shadow-sm rounded-0">
                                <div class="card-body">
                                    <h6 class="fw-bold">👨‍🏫 Tài khoản giảng viên</h6>
                                    <p class="text-muted small mb-0">
                                        Thêm/xóa giảng viên, phân quyền tài khoản giảng dạy
                                    </p>
                                </div>
                            </div>
                        </div>
                        </a>
                       

                        <!-- Môn học & Lớp học phần -->
                        <a href="{{route('adminhomes.monhoc_lophp')}}">
                        
                              <div class="col">
                            <div class="card h-100 shadow-sm rounded-0">
                                <div class="card-body">
                                    <h6 class="fw-bold">📚 Môn học & Lớp học phần</h6>
                                    <p class="text-muted small mb-0">
                                        Danh sách môn học, lớp học phần và phân công giảng viên
                                    </p>
                                </div>
                            </div>
                        </div>
                        </a>

                      

                        <!-- Giám sát nhận diện -->
                        <a href="{{route('adminhomes.giamsat_nhandien')}}">
                             <div class="col">
                            <div class="card h-100 shadow-sm rounded-0">
                                <div class="card-body">
                                    <h6 class="fw-bold">🖥️ Giám sát nhận diện</h6>
                                    <p class="text-muted small mb-0">
                                        Truy vết nhận diện camera, xử lý sai điểm danh theo thời gian
                                    </p>
                                </div>
                            </div>
                        </div> 
                        </a>
                       

                        <!-- Dữ liệu điểm danh -->
                        <a href="{{route('adminhomes.dulieudiemdanh')}}">
                             <div class="col">
                            <div class="card h-100 shadow-sm rounded-0">
                                <div class="card-body">
                                    <h6 class="fw-bold">📑 Dữ liệu điểm danh</h6>
                                    <p class="text-muted small mb-0">
                                        Xem lịch sử điểm danh, xuất file Excel, lọc theo ngày/lớp
                                    </p>
                                </div>
                            </div>
                        </div>
                        </a>
                       

                    </div> <!-- end row -->
                </div>
            </div>
        </div>
    </div>

 
</div>
@endsection
