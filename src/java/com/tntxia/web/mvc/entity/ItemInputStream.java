package com.tntxia.web.mvc.entity;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Closeable;

import com.tntxia.web.mvc.entity.MultipartStream.MalformedStreamException;

/**
 * An {@link InputStream} for reading an items contents.
 */
public class ItemInputStream extends InputStream implements Closeable {
	
	MultipartStream multi;

    /**
     * The number of bytes, which have been read so far.
     */
    private long total;

    /**
     * The number of bytes, which must be hold, because
     * they might be a part of the boundary.
     * 
     * 保留的距离 分隔符  + 2
     * 
     */
    private int pad;

    /**
     * The current offset in the buffer.
     */
    private int pos;
    
    /**
     * The amount of data, in bytes, that must be kept in the buffer in order
     * to detect delimiters reliably.
     */
    private int keepRegion;

    /**
     * Whether the stream is already closed.
     */
    private boolean closed;

    /**
     * Creates a new instance.
     */
    ItemInputStream(MultipartStream multi) {
    	this.multi = multi;
    	this.keepRegion = multi.getBoundary().length;
        findSeparator();
    }

    /**
     * Called for finding the separator.
     */
    private void findSeparator() {
    	int tail = multi.getTail();
    	int head = multi.getHead();
        pos = multi.findSeparator();
        if (pos == -1) {
            if (tail - head > keepRegion) {
                pad = keepRegion;
            } else {
                pad = tail - head;
            }
        }
    }

    /**
     * Returns the number of bytes, which have been read
     * by the stream.
     *
     * @return Number of bytes, which have been read so far.
     */
    public long getBytesRead() {
        return total;
    }

    /**
     * Returns the number of bytes, which are currently
     * available, without blocking.
     *
     * @throws IOException An I/O error occurs.
     * @return Number of bytes in the buffer.
     */
    @Override
    public int available() throws IOException {
    	int head = multi.getHead();
        if (pos == -1) {
        	// 缓存中的可用数量
        	int availableNum = multi.available();
            return availableNum - pad;
        }
        return pos - head;
    }

    /**
     * Offset when converting negative bytes to integers.
     */
    private static final int BYTE_POSITIVE_OFFSET = 256;

    /**
     * Returns the next byte in the stream.
     *
     * @return The next byte in the stream, as a non-negative
     *   integer, or -1 for EOF.
     * @throws IOException An I/O error occurred.
     */
    @Override
    public int read() throws IOException {
        if (closed) {
            throw new FileItemStream.ItemSkippedException();
        }
        if (available() == 0 && makeAvailable() == 0) {
            return -1;
        }
        ++total;
        int b = multi.read();
        if (b >= 0) {
            return b;
        }
        return b + BYTE_POSITIVE_OFFSET;
    }

    /**
     * Reads bytes into the given buffer.
     *
     * @param b The destination buffer, where to write to.
     * @param off Offset of the first byte in the buffer.
     * @param len Maximum number of bytes to read.
     * @return Number of bytes, which have been actually read,
     *   or -1 for EOF.
     * @throws IOException An I/O error occurred.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new FileItemStream.ItemSkippedException();
        }
        if (len == 0) {
            return 0;
        }
        int res = available();
        if (res == 0) {
            res = makeAvailable();
            if (res == 0) {
                return -1;
            }
        }
        res = Math.min(res, len);
        
        multi.readToBuffer(b, off, res);
        System.out.println("pos:" + pos);
        multi.showBuffer(pos);
        multi.showHeadTail("after read,,,");
        // multi.showBuffer();
        total += res;
        return res;
    }

    /**
     * Closes the input stream.
     *
     * @throws IOException An I/O error occurred.
     */
    @Override
    public void close() throws IOException {
        close(false);
    }

    /**
     * Closes the input stream.
     *
     * @param pCloseUnderlying Whether to close the underlying stream
     *   (hard close)
     * @throws IOException An I/O error occurred.
     */
    public void close(boolean pCloseUnderlying) throws IOException {
        if (closed) {
            return;
        }
        if (pCloseUnderlying) {
            closed = true;
            multi.getInputStream().close();
        } else {
            for (;;) {
                int av = available();
                if (av == 0) {
                    av = makeAvailable();
                    if (av == 0) {
                        break;
                    }
                }
                skip(av);
            }
        }
        closed = true;
    }

    /**
     * Skips the given number of bytes.
     *
     * @param bytes Number of bytes to skip.
     * @return The number of bytes, which have actually been
     *   skipped.
     * @throws IOException An I/O error occurred.
     */
    @Override
    public long skip(long bytes) throws IOException {
        if (closed) {
            throw new FileItemStream.ItemSkippedException();
        }
        int av = available();
        if (av == 0) {
            av = makeAvailable();
            if (av == 0) {
                return 0;
            }
        }
        long res = Math.min(av, bytes);
        multi.skip(res);
        return res;
    }

    /**
     * Attempts to read more data.
     *
     * @return Number of available bytes
     * @throws IOException An I/O error occurred.
     */
    private int makeAvailable() throws IOException {
    	
        if (pos != -1) {
            return 0;
        }
        
        multi.keepPad(pad);
        multi.resetTail(pad);

        for (;;) {
            int bytesRead = multi.resetBuffer(pad);
            
            if (bytesRead == -1) {
                // The last pad amount is left in the buffer.
                // Boundary can't be in there so signal an error
                // condition.
                final String msg = "Stream ended unexpectedly";
                throw new MalformedStreamException(msg);
            }
            multi.noteBytesRead(bytesRead);
            multi.translateTail(bytesRead);
            
            // printOutBuffer();
            findSeparator();
            int av = available();

            if (av > 0 || pos != -1) {
                return av;
            }
        }
    }

    /**
     * Returns, whether the stream is closed.
     *
     * @return True, if the stream is closed, otherwise false.
     */
    public boolean isClosed() {
        return closed;
    }

}
