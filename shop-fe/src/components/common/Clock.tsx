import React from 'react';
import { useEffect, useState } from 'react';
import moment from 'moment';

const Clock = () => {
  const [time, setTime] = useState(new Date());

  useEffect(() => {
    const interval = setInterval(() => setTime(new Date()), 1000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-inherit rounded-lg">
      <div className="text-md font-semibold">{moment(time).format('DD/MM/YYYY HH:mm:ss')}</div>
    </div>
  );
};

export default Clock;
