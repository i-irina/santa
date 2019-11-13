class Reminder extends Thread {
        public static final int STOP = 0;
        public static final int RUN = 1;
        public static final int WAIT = 2;

        protected int state = WAIT;
        protected long delay;

        public Reminder(long seconds) {
            delay = seconds;
            state = RUN;
            start();
        }

//наработки на будущее
        public synchronized int checkState() {
            while (state == WAIT) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            return state;
        }

        public void scan() {
            // пулим папку
            //System.out.println("проверили");
            MakePresent.checkDir();
        }

        public void run() {
            while (checkState() != STOP) {
                try {
                    this.sleep(delay);
                } catch (InterruptedException e) {
                }
               //запускаем сканирование папки на появление новых файлов
                scan();
            }
        }
    }
