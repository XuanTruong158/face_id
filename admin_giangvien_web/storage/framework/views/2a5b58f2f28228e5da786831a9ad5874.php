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

<?php echo $__env->make('layouts.app', array_diff_key(get_defined_vars(), ['__data' => 1, '__path' => 1]))->render(); ?><?php /**PATH C:\Laravel\admin_giangvien\resources\views/adminhomes/giamsat_nhandien.blade.php ENDPATH**/ ?>