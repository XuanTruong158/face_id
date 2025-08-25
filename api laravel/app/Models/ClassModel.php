<?php

namespace App\Models;

use MongoDB\Laravel\Auth\User as Authenticatable;

use Illuminate\Database\Eloquent\Factories\HasFactory;

class ClassModel extends Authenticatable
{
    use HasFactory;
    protected $connection = 'mongodb';
    protected $collection = 'classes';

    protected $fillable = [
        '_id',
        'courseId',
        'lectureId',
        'name',
        'semester',
        'year',    
    ];
    // mqh vs course
    public function course() {
        return $this->belongsTo(Course::class, 'courseId', '_id');
    }
    // mqh vs lecture
    public function lecture() {
        return $this->belongsTo(User::class, 'lectureId', '_id');
    }

}
