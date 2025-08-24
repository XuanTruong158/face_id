@extends('layouts.app')

@section('content')
<div class="container mt-4">
    <div class="card shadow-sm">
        <div class="card-header d-flex justify-content-between align-items-center bg-light">
            <h5 class="mb-0 ">Dữ liệu điểm danh</h5>
        </div>

        <div class="card-body">
           <!-- Thanh tìm kiếm + Bộ lọc -->
<div class="container mt-4 mb-3">
  <form class="row g-2 align-items-center justify-content-center">

                <div class="col-md-2">
                    <div class="col-md-auto">
                <input type="date" class="form-control" placeholder="dd/mm/yyyy">
            </div>
                </div>
            
                <!-- Bộ lọc danh mục -->
                <div class="col-md-2">
                <select class="form-select">
                    <option selected>Chọn môn học</option>
                    <option value="1">Danh mục 1</option>
                    <option value="2">Danh mục 2</option>
                    <option value="3">Danh mục 3</option>
                </select>
                </div>

                <div class="col-md-2">
                <select class="form-select">
                    <option selected>Chọn lớp học phần</option>
                    <option value="1">Danh mục 1</option>
                    <option value="2">Danh mục 2</option>
                    <option value="3">Danh mục 3</option>
                </select>
                </div>



                <!-- Bộ lọc trạng thái -->
                <div class="col-md-2">
                <select class="form-select">
                    <option selected>Chọn trạng thái</option>
                    <option value="active">Hoạt động</option>
                    <option value="inactive">Không hoạt động</option>
                </select>
                </div>
                    <!-- Ô tìm kiếm -->
                <div class="col-md-4">
                <div class="input-group">
                    <input class="form-control" type="search" placeholder="Tìm kiếm..." aria-label="Search">
                    <button class="btn btn-primary" type="submit">Tìm</button>
                </div>
                </div>


            </form>
            </div>


            <!-- Bảng dữ liệu -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle text-center">
                    <thead class="table-info">
                        <tr>
                            <th>THời gian</th>
                            <th>Mã sinh viên</th>
                            <th>Họ tên</th>
                            <th>Môn học</th>
                            <th>Lớp </th>
                            <th>Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        @forelse ($students as $student)    
                            <tr>
                                <td>{{ $student['id'] }}</td>
                                <td>{{ $student['name'] }}</td>
                                <td>{{ $student['class'] }}</td>
                                <td>{{ $student['email'] ?: 'Chưa có' }}</td>
                                <td>
                                    @if($student['status'] == 1)
                                        <span class="text-success fw-bold">Đang hoạt động</span>
                                    @else
                                        <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                    @endif
                                </td>
                                <td>
                                    <a href="#" class="btn btn-success btn-sm">Sửa</a>
                                    <a href="#" class="btn btn-danger btn-sm">Xóa</a>
                                </td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="6" class="text-center">Không có dữ liệu sinh viên</td>
                            </tr>
                        @endforelse                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection
