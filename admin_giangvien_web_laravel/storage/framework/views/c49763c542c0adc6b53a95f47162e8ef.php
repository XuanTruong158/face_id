<?php $__env->startSection('content'); ?>
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
                        <?php $__empty_1 = true; $__currentLoopData = $students; $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $student): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); $__empty_1 = false; ?>    
                            <tr>
                                <td><?php echo e($student['id']); ?></td>
                                <td><?php echo e($student['name']); ?></td>
                                <td><?php echo e($student['class']); ?></td>
                                <td><?php echo e($student['email'] ?: 'Chưa có'); ?></td>
                                <td>
                                    <?php if($student['status'] == 1): ?>
                                        <span class="text-success fw-bold">Đang hoạt động</span>
                                    <?php else: ?>
                                        <span class="text-danger fw-bold">Ngừng hoạt động</span>
                                    <?php endif; ?>
                                </td>
                                <td>
                                    <a href="#" class="btn btn-success btn-sm">Sửa</a>
                                    <a href="#" class="btn btn-danger btn-sm">Xóa</a>
                                </td>
                            </tr>
                        <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); if ($__empty_1): ?>
                            <tr>
                                <td colspan="6" class="text-center">Không có dữ liệu sinh viên</td>
                            </tr>
                        <?php endif; ?>                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<?php $__env->stopSection(); ?>

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/adminhomes/dulieudiemdanh.blade.php ENDPATH**/ ?>