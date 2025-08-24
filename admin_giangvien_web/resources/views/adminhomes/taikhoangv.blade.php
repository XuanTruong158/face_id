@extends('layouts.app')

@section('content')
<div class="container mt-4">
    <div class="card shadow-sm">
     <div class="card-header d-flex justify-content-between align-items-center bg-light">
    <h5 class="mb-0">📋 Danh sách tài khoản giảng viên</h5>
    <a href="{{route('giangviens.create')}}" class="btn btn-info btn-sm text-white">
        ➕ Thêm giảng viên mới
    </a>
</div>


        <div class="card-body">
            <!-- Ô tìm kiếm -->
            <div class="mb-3">
                <input type="text" class="form-control w-25" placeholder="🔍 Tìm kiếm giảng viên...">
            </div>

            <!-- Bảng dữ liệu -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle text-center">
                    <thead class="table-info">
                        <tr>
                            <th>Mã GV</th>
                            <th>Họ tên</th>
                            <th>Lớp</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        @forelse ($giangviens as $giangvien)    
                                <tr>
                                    <td>{{ $giangvien['id'] }}</td>
                                    <td>{{ $giangvien['name'] }}</td>
                                    <td>{{ $giangvien['monhoc'] }}</td>
                                    <td>{{ $giangvien['email'] }}</td>
                                    <td>
                                        @if($giangvien['status'] == 1)
                                            <span class="text-success fw-bold">Đang hoạt động</span>
                                        @else
                                            <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                        @endif
                                    </td>
                                    <td>
                                        <!-- Nút sửa -->
                                        <a href="{{ route('giangviens.edit', $giangvien['id']) }}" class="btn btn-success btn-sm">Sửa</a>

                                        <!-- Nút xóa -->
                                        <form action="{{ route('giangviens.destroy', $giangvien['id']) }}" 
                                            method="POST" class="d-inline-block">
                                            @csrf
                                            @method('DELETE')
                                            <button type="submit" 
                                                    class="btn btn-sm btn-danger" 
                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa giảng viên này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                @empty
                                <tr>
                                    <td colspan="6" class="text-center">Không có dữ liệu giảng viên</td>
                                </tr>
                                @endforelse

                      

                 
                 
                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection
