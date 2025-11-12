import React from 'react';

export default function Spinner() {
  return (
    <div className="spinner-border animate-spin inline-block w-8 h-8 border-4 rounded-full text-gray-300" role="status">
      <span className="sr-only">Loading...</span>
    </div>
  );
}
