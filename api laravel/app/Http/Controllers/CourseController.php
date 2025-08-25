<?php

namespace App\Http\Controllers;

use App\Models\Course;
use Illuminate\Http\Request;

class CourseController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        return response()->json(Course::all());
    }


    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $course = Course::create($request->all());
        return response()->json($course);
             
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        return response()->json(Course::find($id));
        
    }



    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        
        $course = Course::find($id);
        $course->update($request->all());
        return response()->json($course);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $course = Course::find($id);
    
        $course->delete();
        return response()->json(['message' => 'Course deleted']);
    }
}
