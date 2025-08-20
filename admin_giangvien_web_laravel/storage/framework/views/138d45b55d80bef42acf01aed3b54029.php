<?php $__env->startSection('content'); ?>
<div class="container mt-4">
    <div class="card shadow rounded-3">
        <div class="card-body" style="background-color: #d4f8f8;">
            <h4 class="text-center fw-bold mb-4">Tạo lớp học</h4>
            <form action="<?php echo e(route('lophocphans.store')); ?>" method="POST">
                <?php echo csrf_field(); ?>
                <div class="row">
                    <!-- Cột trái -->
                    <div class="col-md-6">
                        <!-- Môn học -->
                        <div class="mb-3">
                            <label class="form-label">Môn học</label>
                            <select class="form-select">
                                <option selected>Chọn môn học</option>
                                <option>Toán</option>
                                <option>Lý</option>
                                <option>Hóa</option>
                            </select>
                        </div>

                        <!-- Ngày bắt đầu -->
                        <div class="mb-3">
                            <label class="form-label">Ngày bắt đầu</label>
                            <div class="d-flex gap-2">
                                <input type="number" class="form-control" placeholder="Ngày" min="1" max="31">
                                <input type="number" class="form-control" placeholder="Tháng" min="1" max="12">
                                <input type="number" class="form-control" placeholder="Năm">
                            </div>
                        </div>
                    </div>

                    <!-- Cột phải -->
                    <div class="col-md-6">
                        <!-- Lớp học phần -->
                        <div class="mb-3">
                            <label class="form-label">Lớp học phần</label>
                            <input type="text" class="form-control" placeholder="Nhập tên lớp học phần">
                        </div>

                        <!-- Ngày học trong tuần -->
                        <div class="mb-3">
                            <label class="form-label">Ngày học trong tuần</label>
                            <input type="text" class="form-control" placeholder="Nhập thứ">
                        </div>

                        <!-- Ca học -->
                        <div class="mb-3">
                            <label class="form-label">Ca học</label>
                            <input type="text" class="form-control" placeholder="Nhập ca học">
                        </div>

                        <!-- Ngày kết thúc -->
                        <div class="mb-3">
                            <label class="form-label">Ngày kết thúc</label>
                            <div class="d-flex gap-2">
                                <input type="number" class="form-control" placeholder="Ngày" min="1" max="31">
                                <input type="number" class="form-control" placeholder="Tháng" min="1" max="12">
                                <input type="number" class="form-control" placeholder="Năm">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Nút -->
                <div class="text-end mt-3">
                    <button type="submit" class="btn btn-success">Tạo lớp học</button>
                    <a href="<?php echo e(route('adminhomes.monhoc_lophp')); ?>" class="btn btn-secondary">Hủy</a>
                </div>
            </form>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>
<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/lophocphans/create.blade.php ENDPATH**/ ?>