@extends('layouts.app')

@section('content')
<div class="container mt-4">
    <div class="card shadow-sm">
     <div class="card-header d-flex justify-content-between align-items-center bg-light">
    <h5 class="mb-0">📋 Danh sách tài khoản sinh viên</h5>
    <a href="{{route('students.create')}}" class="btn btn-info btn-sm text-white">
        ➕ Thêm sinh viên
    </a>
</div>


        <div class="card-body">
            <!-- Ô tìm kiếm -->
            <div class="mb-3">
                <input type="text" class="form-control w-25" placeholder="🔍 Tìm kiếm sinh viên...">
            </div>

            <!-- Bảng dữ liệu -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle text-center">
                    <thead class="table-info">
                        <tr>
                            <th>Mã SV</th>
                            <th>Họ tên</th>
                            <th>Lớp</th>
                            <th>Email</th>
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
                                    <td>{{ $student['email'] }}</td>
                                    <td>
                                        @if($student['status'] == 1)
                                            <span class="text-success fw-bold">Đang hoạt động</span>
                                        @else
                                            <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                        @endif
                                    </td>
                                    <td>
                                        <!-- Nút sửa -->
                                        <a href="{{ route('students.edit', $student['id']) }}" class="btn btn-success btn-sm">Sửa</a>

                                        <!-- Nút xóa -->
                                        <form action="{{ route('students.destroy', $student['id']) }}" 
                                            method="POST" class="d-inline-block">
                                            @csrf
                                            @method('DELETE')
                                            <button type="submit" 
                                                    class="btn btn-sm btn-danger" 
                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </button>
                                        </form>
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
