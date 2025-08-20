@extends('layouts.app')

@section('content')
<div class="container mt-4">
    <div class="card shadow-sm">
     <div class="card-header d-flex justify-content-between align-items-center bg-light">
    <h5 class="mb-0">Quản lý môn học & lớp học</h5>
    <a href="{{route('lophocphans.create')}}" class="btn btn-info btn-sm text-white">
        ➕ Thêm lớp học phần mới
    </a>    
</div>A


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
                            <th>Mã môn</th>
                            <th>Tên môn </th>
                            <th>Mã lớp học </th>
                            <th>Số tín</th>
                            <th>Giáo viên phụ trách</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        @forelse ($monhocs as $monhoc)    
                                <tr>
                                    <td>{{ $monhoc['id'] }}</td>
                                    <td>{{ $monhoc['name'] }}</td>
                                    <td>{{ $monhoc['monhoc'] }}</td>
                                    <td>{{ $monhoc['lophocphan'] }}</td>
                                    <td>
                                        @if($monhoc['status'] == 1)
                                            <span class="text-success fw-bold">Đang hoạt động</span>
                                        @else
                                            <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                        @endif
                                    </td>
                                    <td>
                                        <!-- Nút sửa -->
                                        <a href="{{ route('monhocs.edit', $monhoc['id']) }}" class="btn btn-success btn-sm">Sửa</a>

                                        <!-- Nút xóa -->
                                        <form action="{{ route('monhocs.destroy', $monhoc['id']) }}" 
                                            method="POST" class="d-inline-block">
                                            @csrf
                                            @method('DELETE')
                                            <button type="submit" 
                                                    class="btn btn-sm btn-danger" 
                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa lớp học này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                @empty
                                <tr>
                                    <td colspan="6" class="text-center">Không có dữ liệu lớp học</td>
                                </tr>
                                @endforelse                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection
