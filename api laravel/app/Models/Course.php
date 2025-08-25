<?php

namespace App\Models;
use MongoDB\Laravel\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;


use Illuminate\Database\Eloquent\Factories\HasFactory;

class Course extends Authenticatable
{
    use HasFactory;
    protected $connection = 'mongodb';
    protected $collection = 'users';

    protected $fillable = [
        '_id',
        'code',
        'name',
        'credits',
        'describe',
    
    ];
}
