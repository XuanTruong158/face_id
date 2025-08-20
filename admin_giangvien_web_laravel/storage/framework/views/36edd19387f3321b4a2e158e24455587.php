<?php $__env->startSection('content'); ?>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header bg-light">
                    <h3 class="mb-0">Bảng điều khiển quản trị viên</h3>
                </div>
                <div class="card-body" style="background-color:#b3d9ff;">
                    <div class="row text-center">

                        <!-- Tài khoản sinh viên -->
                        <div class="col-md-2 col-sm-6 mb-3">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h6 class="fw-bold">📘 Tài khoản sinh viên</h6>
                                    <p class="text-muted small">
                                        Quản lý danh sách sinh viên, thông tin đăng nhập, trạng thái
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Tài khoản giảng viên -->
                        <div class="col-md-2 col-sm-6 mb-3">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h6 class="fw-bold">👨‍🏫 Tài khoản giảng viên</h6>
                                    <p class="text-muted small">
                                        Thêm/xóa giảng viên, phân quyền tài khoản giảng dạy
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Môn học & Lớp học phần -->
                        <div class="col-md-2 col-sm-6 mb-3">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h6 class="fw-bold">📚 Môn học & Lớp học phần</h6>
                                    <p class="text-muted small">
                                        Danh sách môn học, lớp học phần và phân công giảng viên
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Giám sát nhận diện -->
                        <div class="col-md-2 col-sm-6 mb-3">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h6 class="fw-bold">🖥️ Giám sát nhận diện</h6>
                                    <p class="text-muted small">
                                        Truy vết nhận diện camera, xử lý sai điểm danh theo thời gian
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Dữ liệu điểm danh -->
                        <div class="col-md-2 col-sm-6 mb-3">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h6 class="fw-bold">📑 Dữ liệu điểm danh</h6>
                                    <p class="text-muted small">
                                        Xem lịch sử điểm danh, xuất file Excel, lọc theo ngày/lớp
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div> <!-- end row -->
                </div>
            </div>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/welcome.blade.php ENDPATH**/ ?>