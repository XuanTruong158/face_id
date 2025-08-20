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
<div class="header-bar">
        <i class="bi bi-x-lg"></i>
        <h4>Giám sát nhận diện (theo thời gian thực)</h4>
    </div>

    <div class="content-area">
        <div class="row">
            <div class="col-md-7">
                <div class="video-box rounded shadow-sm">
                    <div class="video-overlay-text">Camera đang hoạt động</div>
                    </div>
            </div>

            <div class="col-md-5">
                <div class="info-box shadow-sm">
                    <div><strong>Lần nhận diện gần nhất:</strong> 08:42:15 - 05/08/2025</div>
                    <div><strong>Trạng thái:</strong> <span class="status-success">✓ Nhận diện thành công</span></div>
                    <div><strong>Sinh viên:</strong> Nguyễn Văn A - DHTL-01</div>
                </div>
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
