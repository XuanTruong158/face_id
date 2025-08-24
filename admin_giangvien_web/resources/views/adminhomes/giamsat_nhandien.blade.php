@extends('layouts.app')

@section('content')
<div class="container mt-4">

    <!-- Thanh giám sát + tìm kiếm + bộ lọc -->
    <div class="mb-4">
        <div class="card shadow-sm">
            <div class="card-header bg-light d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Giám sát nhận diện (theo thời gian thực)</h5>
                <i class="bi bi-x-lg fs-5"></i>
            </div>

            <div class="card-body">
                <div class="row g-3">
                    <!-- Video Camera -->
                    <div class="col-md-7">
                        <div class="video-box rounded shadow-sm position-relative" style="height: 300px; background: #000;">
                            <div class="video-overlay-text position-absolute top-50 start-50 translate-middle text-white fw-bold">
                                Camera đang hoạt động
                            </div>
                        </div>
                    </div>

                    <!-- Thông tin lần nhận diện -->
                    <div class="col-md-5">
                        <div class="info-box shadow-sm p-3 rounded bg-light">
                            <p class="mb-2"><strong>Lần nhận diện gần nhất:</strong> 08:42:15 - 05/08/2025</p>
                            <p class="mb-2"><strong>Trạng thái:</strong> <span class="text-success fw-bold">✓ Nhận diện thành công</span></p>
                            <p class="mb-2"><strong>Sinh viên:</strong> Nguyễn Văn A - DHTL-01</p>

                            <!-- Thanh tìm kiếm -->
                            <form class="d-flex mt-3">
                                <input type="text" class="form-control me-2" placeholder="Tìm kiếm sinh viên...">
                                <button type="submit" class="btn btn-primary">Tìm</button>
                            </form>

                            <!-- Bộ lọc trạng thái -->
                            <div class="mt-3">
                                <label for="filterStatus" class="form-label fw-bold">Bộ lọc trạng thái:</label>
                                <select id="filterStatus" class="form-select">
                                    <option value="">Tất cả</option>
                                    <option value="1">Đang hoạt động</option>
                                    <option value="0">Ngừng hoạt động</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>  
    </div>

    <!-- Bảng dữ liệu sinh viên -->
    <div class="card shadow-sm">
        <div class="card-header bg-light">
            <h5 class="mb-0">Dữ liệu điểm danh</h5>
        </div>

        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle text-center">
                    <thead class="table-info">
                        <tr>
                            <th>Thời gian</th>
                            <th>Mã sinh viên</th>
                            <th>Họ tên</th>
                            <th>Môn học</th>
                            <th>Lớp</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
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
                                <td colspan="7" class="text-center">Không có dữ liệu sinh viên</td>
                            </tr>
                        @endforelse                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection
